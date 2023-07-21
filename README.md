# 고가용성 대용량 분산 처리 채팅 서버
**프로젝트 상세 내용은 아래 노션 링크에서 확인하실 수 있습니다!**

[프로젝트 상세 설명 페이지](https://www.notion.so/301174044fb84971a134f8b817d9ade9?pvs=21)

# 소개

Kubernetes, kafka 등 인프라에서 사용되는 기술에 대한 깊이있는 학습과 Enterprise 규모에서 사용할 수 있는 시스템 설계 역량을 기르기 위해 시작한 프로젝트입니다.
이를 위해, 초당 수만 건 이상의 트래픽으로 부하테스트를 진행하며 성능을 개선 시켜나갔으며 Scale in/out, Failover 전략, 모니터링, 배포 방식 등을 고려하면서 서버를 설계해나갔습니다. 설계만 한 것이 아니라 AWS EKS를 이용해 실제로 설계한 내용들을 클라우드 환경에 구현하였습니다.

# 기술 스택

Spring Boot, Kubernetes, Kafka, Docker, Redis, PostgreSQL, Prometheus, Grafana, Jmeter, Jenkins, AWS

# 인프라 구성

# 채팅 파이프라인

<img src="https://chat-practice-image.s3.ap-northeast-2.amazonaws.com/%E1%84%8B%E1%85%B0%E1%86%B8%E1%84%89%E1%85%A9%E1%84%8F%E1%85%A6%E1%86%BA+%E1%84%86%E1%85%AE%E1%86%AB%E1%84%8C%E1%85%A6+%E1%84%92%E1%85%A2%E1%84%80%E1%85%A7%E1%86%AF%E1%84%83%E1%85%AC%E1%86%AB+%E1%84%80%E1%85%AE%E1%84%8C%E1%85%A9(%E1%84%86%E1%85%A2%E1%84%91%E1%85%B5%E1%86%BC%E1%84%90%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%87%E1%85%B3%E1%86%AF+%E1%84%87%E1%85%A1%E1%86%BC%E1%84%89%E1%85%B5%E1%86%A8)+(1).jpg"  width="80%" height="80%"/>

### 전달 과정

1. 유저가 소켓으로 채팅을 보내면 WAS가 Chat-send-topic에 채팅 내용을 produce 합니다.
2. Message classfier는 Chat-send-topic에서 메시지를 consume 하고 해당 채팅방에 있는 유저들에 대해 반복문을 돌면서 Chat-receive-topic에 메시지를 다시 생산합니다.
3. 이 때, Message classfier는 Routing table을 보고 수신자가 연결되어 있는 WAS_ID와 동일한 파티션 번호에 produce 합니다.(WAS와 파티션이 매핑되어있기 때문에)
4. WAS는 자신과 매핑된 Chat-receive-topic 파티션의 메시지를 소비해서 소켓을 통해 수신자에서 채팅 메시지를 전달합니다.

### Scale out

소켓 커넥션으로 인해 WAS가 상태를 가져 수평확장 방식을 설계하는데 어려움이 있었지만
Routing table과 WAS-Partition 일대일 매핑 그리고 Message classifier를 통해 수평확장 가능한 구조로 만들었습니다.

### Failover

WAS에 소켓도 연결되어 있고 파티션과의 매핑 관계도 유지해야 하기 때문에 WAS 장애 시 적절한 failover 전략이 필요했습니다. 우선, StickyAssignor를 사용해 WAS 장애 발생으로 인한 파티션 리밸런싱이 일어날 때 WAS와 파티션 간의 매핑 관계가 꼬이지 않게 해주었습니다. 그리고, WAS의 연결된 소켓은 클라이언트 단에서 즉시 다른 정상 WAS와 재연결을 맺어주어 장애 복구 시간을 최소화 하였습니다.

### 병렬처리

chat-send-topic에 들어간 채팅 메시지는 Message classifier에서 병렬적으로 consuming 되기 때문에 메시지 간 순서가 보장되지 않는 문제가 있었습니다. 이를 해결하기 위해 WAS에서 Chat-send-topic에 produce 할 때 key 값으로 채팅방id를 넣어 같은 채팅방id를 가진 메시지는 동일한 파티션으로 들어가게 만들었고 최소한 같은 채팅방 내 메시지들의 순서는 보장할 수 있게 되었습니다.

**더 자세한 내용은 아래 노션 페이지에서 확인하실 수 있습니다.**

[웹소켓 Scale out 전략](https://www.notion.so/Scale-out-8d9fcc178d72470bbaac325fbc446d10?pvs=21)
