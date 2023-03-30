import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'



var url = "/gs-guide-websocket";
let stompClient = Stomp.client(url);


const handlers = []

export function connect() {
    stompClient.webSocketFactory = function () {
        return new SockJS("/gs-guide-websocket");
    };
    stompClient.debug = () => {}
    stompClient.connect({}, frame => {
        stompClient.subscribe('/topic/activity', message => {
            handlers.forEach(handler => handler(JSON.parse(message.body)));
        });
    });
}

export function addHandler(handler) {
    handlers.push(handler)
}


export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

// export function sendMessage(message) {
//     stompClient.send("/app/change_message", {}, JSON.stringify(message));
// }
