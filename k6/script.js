import ws from 'k6/ws';
import { check } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

export function handleSummary(data) {

    return {

        "summary.html": htmlReport(data),

    }

}

export const options = {
    iterations: 800,
    vus: 800
};

export default function () {
    const url = 'ws://localhost:8080/ws';

        const params = { };
    console.log('Start');
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
                socket.send('SEND\n' +
                    'destination:/pub/chat/message\n' +
                    '\n' +
                    '{\n' +
                    '    "chatRoomId":1,\n' +
                    '    "content":"할로우"\n' +
                    '}\n\n\0');
            }, 4);

            // socket.send('SEND\n' +
            //     'destination:/pub/chat/message\n' +
            //     '\n' +
            //     '{\n' +
            //     '    "chatRoomId":1,\n' +
            //     '    "content":"할로우"\n' +
            //     '}\n\n\0');


        });

        // socket.on('message',
        //     (data)=>console.log(data)
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
        }, 2000);  // 30초 후에 타임아웃됩니다.
    });

    check(res, { 'WebSocket connection established': (r) => r && r.status === 101 });
}