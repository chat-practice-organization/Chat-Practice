import ws from 'k6/ws';
import { check } from 'k6';
import { Counter } from 'k6/metrics';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

// export function handleSummary(data) {
//
//     return {
//
//         "summary.html": htmlReport(data),
//
//     }
//
// }

export const options = {
    iterations: 100,
    vus: 100
};

export default function () {
    const url = 'ws://aca5f9121c2df4fe6a33da6b72c5548c-1086589856.ap-northeast-2.elb.amazonaws.com/ws';
        const params = { };
    console.log('Start');
    let isCacheClear = false;
    const res = ws.connect(url, params, function(socket) {
        socket.on('open', function() {
            console.log('WebSocket connection opened.');

            // STOMP 연결 프레임
            socket.send('CONNECT\n' +
                'host:/\n' +
                'login:\n' +
                'passcode:\n' +
                'accept-version:1.0,1.1,1.2\n' +
                'heart-beat:6000,0\n\n\0');

            socket.send('SUBSCRIBE\n' +
                'id:sub-0\n' +
                'destination:/sub/chat\n\n\0');


            socket.setInterval(function timeout() {
                if (!isCacheClear) {
                    console.log("clear cache")
                    socket.send('SEND\n' +
                        'destination:/pub/chat/cache\n' +
                        '\n' +
                        '{\n' +
                        '    "roomId":1,\n' +
                        '    "action":"enter"\n' +
                        '}\n\n\0');
                    isCacheClear=true;
                }
                socket.send('SEND\n' +
                    'destination:/pub/chat/message\n' +
                    '\n' +
                    '{\n' +
                    '    "chatRoomId":1,\n' +
                    '    "content":"할로우"\n' +
                    '}\n\n\0');
            }, 1000);

            // socket.send('SEND\n' +
            //     'destination:/pub/chat/message\n' +
            //     '\n' +
            //     '{\n' +
            //     '    "chatRoomId":1,\n' +
            //     '    "content":"할로우"\n' +
            //     '}\n\n\0');


        });

        // socket.on('message',
        //     (data)=> {
        //
        //     }
        // )

        socket.on('close', function() {
            console.log('WebSocket connection closed.');
        });

        socket.on('error', function(e) {
            console.error('Error occurred:', e.error());
        });

        socket.setTimeout(function() {
            console.log('Socket timed out.');
            socket.close();
        }, 30000);  // 30초 후에 타임아웃됩니다.
    });

    check(res, { 'WebSocket connection established': (r) => r && r.status === 101 });
}