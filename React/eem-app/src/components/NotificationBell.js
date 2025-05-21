import { useContext, useEffect, useState, useCallback } from "react";
import { Badge, Dropdown, Spinner, ListGroup } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";
import { BiBell } from "react-icons/bi";

const NotificationBell = () => {
  const user = useContext(MyUserContext);
  const navigate = useNavigate();
  const location = useLocation();
  const [count, setCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);

  const fetchNotifications = useCallback(async () => {
    if (!user?.id) {
      setError("Không tìm thấy thông tin người dùng");
      setLoading(false);
      return;
    }

    setLoading(true);
    setNotifications([]);
    setCount(0);

    try {
      let page = 1;
      let allProblems = [];
      let allMaintenances = [];

      while (true) {
        const problemRes = await authApis().get(
          `${endpoints["problemTechnicianId"]}${user.id}`,
          { params: { page } }
        );
        const problems = problemRes.data || [];
        allProblems = [...allProblems, ...problems];

        if (problems.length < 5 || problems.length === 0) break;
        page++;
      }

      page = 1;
      while (true) {
        const maintenanceRes = await authApis().get(
          `${endpoints["maintenanceTechnicianId"]}${user.id}`,
          { params: { page } }
        );
        const maintenances = maintenanceRes.data || [];
        allMaintenances = [...allMaintenances, ...maintenances];

        if (maintenances.length < 5 || maintenances.length === 0) break;
        page++;
      }

      const activeProblems = allProblems.filter((p) => !p.isDone) || [];
      const activeMaintenance =
        allMaintenances.filter((m) => m.maintenanceStatus !== "COMPLETED") || [];

      const problemNotifications = activeProblems.map((p) => ({
        id: `problem-${p.id}`,
        type: "problem",
        deviceName: p.deviceName,
        description: p.description,
        happenedDate: new Date(p.happenedDate).toLocaleDateString(),
        fatalLevel: p.fatalLevel,
      }));

      const maintenanceNotifications = activeMaintenance.map((m) => ({
        id: `maintenance-${m.id}`,
        type: "maintenance",
        deviceName: m.deviceName,
        title: m.tittle,
        description: m.description,
        startDate: new Date(m.startDate).toLocaleDateString(),
        endDate: new Date(m.endDate).toLocaleDateString(),
        maintenanceStatus: m.maintenanceStatus,
      }));

      const allNotifications = [...problemNotifications, ...maintenanceNotifications];
      setNotifications(allNotifications);
      setCount(allNotifications.length);
    } catch (err) {
      console.error("Lỗi khi lấy thông báo:", err);
      setError("Không thể tải thông báo");
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    if (user) {
      fetchNotifications();
    }
  }, [user, fetchNotifications, location.state?.refresh]);

  const handleToggle = () => {
    setShowDropdown(!showDropdown);
  };

  const handleItemClick = (notification) => {
    if (notification.type === "problem") {
      navigate(`/problem-technician/${notification.id.replace("problem-", "")}`);
    } else if (notification.type === "maintenance") {
      navigate(
        `/maintenance-technician/${notification.id.replace("maintenance-", "")}`
      );
    }
    setShowDropdown(false);
  };

  if (error) return null;

  return (
    <Dropdown show={showDropdown} onToggle={handleToggle}>
      <Dropdown.Toggle
        as="div"
        className="position-relative d-inline-flex align-items-center dropdown-toggle-no-caret"
        style={{ cursor: "pointer" }}
      >
        {loading ? (
          <Spinner animation="border" size="sm" />
        ) : (
          <>
            <div style={{ position: "relative" }}>
              <BiBell size={24} />
              {count > 0 && (
                <Badge
                  bg="danger"
                  className="position-absolute top-0 start-100 translate-middle"
                  style={{ fontSize: "0.7rem" }}
                >
                  {count}
                </Badge>
              )}
            </div>
          </>
        )}
      </Dropdown.Toggle>

      <Dropdown.Menu
        style={{ maxHeight: "400px", overflowY: "auto", minWidth: "300px" }}
      >
        {notifications.length === 0 ? (
          <Dropdown.Item disabled>Không có thông báo</Dropdown.Item>
        ) : (
          <ListGroup variant="flush">
            {notifications.map((notification) => (
              <ListGroup.Item
                key={notification.id}
                action
                onClick={() => handleItemClick(notification)}
                className="p-3"
              >
                {notification.type === "problem" ? (
                  <>
                    <strong>Sự cố: {notification.deviceName}</strong>
                    <div>Mô tả: {notification.description}</div>
                    <div>Ngày xảy ra: {notification.happenedDate}</div>
                    <div>Mức độ: {notification.fatalLevel}</div>
                  </>
                ) : (
                  <>
                    <strong>Bảo trì: {notification.deviceName}</strong>
                    <div>Tiêu đề: {notification.title}</div>
                    <div>Bắt đầu: {notification.startDate}</div>
                    <div>Kết thúc: {notification.endDate}</div>
                    <div>Trạng thái: {notification.maintenanceStatus}</div>
                  </>
                )}
              </ListGroup.Item>
            ))}
          </ListGroup>
        )}
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default NotificationBell;