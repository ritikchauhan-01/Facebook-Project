import { Client } from "@stomp/stompjs";
import { useCallback, useEffect } from "react";

let stompClient;
let isConnected = false;
const subscriptions = {};

export function useStomp(path1, path2, callback1, callback2) {
  const connect = useCallback(() => {
    if (!stompClient) {
      stompClient = new Client({
        brokerURL:
          "ws://localhost:6060/facebook/notification-api/received/socket?Authorization=" +
          JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      });
      stompClient.activate();
    }

    stompClient.onConnect = (frame) => {
      isConnected = true;
      const subscription1 = stompClient.subscribe(path1, (message) => {
        const body = JSON.parse(message.body);
        callback1(body);
      });
      subscriptions[path1] = subscription1;

      const subscription2 = stompClient.subscribe(path2, (message) => {
        const body = JSON.parse(message.body);
        callback2(body);
      });
      subscriptions[path2] = subscription2;
    };
  }, []);

  const send = useCallback(
    (path, body, headers) => {
      stompClient.publish({
        destination: path,
        headers,
        body: JSON.stringify(body),
      });
    },
    [stompClient]
  );

  const subscribe = useCallback((path, callback) => {
    console.log("Here", path, stompClient);
    if (!stompClient) return;

    if (subscriptions[path]) return;

    const subscription = stompClient.subscribe(path, (message) => {
      const body = JSON.parse(message.body);
      callback(body);
    });
    subscriptions[path] = subscription;
  }, []);

  const unsubscribe = useCallback((path) => {
    subscriptions[path].unsubscribe();
    delete subscriptions[path];
  }, []);

  const disconnect = useCallback(() => {
    stompClient.deactivate();
  }, [stompClient]);

  useEffect(() => {
    connect();
  }, []);

  return {
    disconnect,
    subscribe,
    unsubscribe,
    subscriptions,
    send,
    isConnected,
  };
}
