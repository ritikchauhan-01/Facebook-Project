import React, { useEffect, useState } from "react";
import "./notification.scss";
import NotificationService from "../../services/notification.service";
import NotifyItem from "../notifyItem/NotifyItem";

const Notification = ({ type }) => {
  const [notifications, setNotifications] = useState(null);

  useEffect(() => {
    if (type === "N")
    NotificationService.getNotifications(0, 8).then((data) => {
      setNotifications(data);
      });
    else if (type === "FR")
      NotificationService.getFRs(0, 8).then((data) => {
        setNotifications(data);
      });
  }, [type]);

  return (
    <div className="notification_wrap">
      <div className="dropdown">
        {notifications && (
          <>
            {notifications.map((n) => (
              <NotifyItem
                n={n}
                key={n.notificationId || n.requestId}
                type={type}
              />
            ))}
          </>
        )}
      </div>
    </div>
  );
};

export default Notification;
