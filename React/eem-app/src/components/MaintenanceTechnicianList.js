import { useContext, useEffect, useState, useCallback } from "react";
import { Card, Button, Spinner } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";

const MaintenanceTechnicianList = () => {
  const [maintenances, setMaintenances] = useState([]);
  const [page, setPage] = useState(1);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(true);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [error, setError] = useState(null);
  const user = useContext(MyUserContext);
  const navigate = useNavigate();
  const location = useLocation();

  const filterDuplicates = useCallback((arr) => {
    const seen = new Set();
    return arr.filter((item) => {
      if (!item.id) return false;
      if (seen.has(item.id)) return false;
      seen.add(item.id);
      return true;
    });
  }, []);

  const loadMaintenances = useCallback(
    async (pageNumber) => {
      if (!user?.id) {
        setError("Không tìm thấy thông tin người dùng");
        setLoading(false);
        return;
      }

      try {
        const url = `${endpoints["maintenanceTechnicianId"]}${user.id}`;
        const res = await authApis().get(url, {
          params: { page: pageNumber },
        });

        if (res.data.length === 0) {
          setHasNext(false);
          return;
        }

        const newData = filterDuplicates(res.data);
        setMaintenances((prev) => filterDuplicates([...prev, ...newData]));

        if (newData.length < 5) {
          setHasNext(false);
        }
      } catch (err) {
        console.error("Lỗi khi tải maintenances:", err);
        setError("Không thể tải danh sách bảo trì");
        setHasNext(false);
      } finally {
        setLoading(false);
        setIsLoadingMore(false);
      }
    },
    [user, filterDuplicates]
  );

  useEffect(() => {
    const handleScroll = () => {
      const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
      if (
        scrollTop + clientHeight >= scrollHeight - 100 &&
        hasNext &&
        !isLoadingMore
      ) {
        setIsLoadingMore(true);
        setPage((prev) => prev + 1);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNext, isLoadingMore]);

  useEffect(() => {
    if (user) {
      setMaintenances([]);
      setPage(1);
      setHasNext(true);
      setLoading(true);
      loadMaintenances(1);
    }
  }, [user, loadMaintenances, location.state?.refresh]);

  useEffect(() => {
    if (page > 1) {
      loadMaintenances(page);
    }
  }, [page, loadMaintenances]);

  const handleNavigateToMaintenance = (maintenanceId) => {
    navigate(`/maintenance-technician/${maintenanceId}`);
  };

  if (loading) return <div className="text-center mt-4"><Spinner animation="border" /></div>;
  if (error) return <div className="text-danger text-center mt-4">{error}</div>;
  if (maintenances.length === 0 && !loading)
    return <div className="text-center mt-4">Không có công việc bảo trì nào được giao</div>;

  return (
    <div className="container mt-4">
      <h4 className="mb-4">Danh sách công việc bảo trì được giao</h4>

      {maintenances.map((m) => (
        <Card key={m.id} className="mb-3 shadow-sm">
          <Card.Body>
            <Card.Title>{m.deviceName}</Card.Title>
            <Card.Text>
              <strong>Tiêu đề:</strong> {m.title}<br />
              <strong>Bắt đầu:</strong> {new Date(m.startDate).toLocaleDateString()}<br />
              <strong>Kết thúc:</strong> {new Date(m.endDate).toLocaleDateString()}<br />
              <strong>Trạng thái:</strong> {m.maintenanceStatus}
            </Card.Text>

            <Button
              onClick={() => handleNavigateToMaintenance(m.id)}
              hidden={!m.isCap}
            >
              {m.maintenanceStatus === "COMPLETED" ? "Đã hoàn thành" : "Xác nhận bảo trì"}
            </Button>
          </Card.Body>
        </Card>
      ))}

      {isLoadingMore && (
        <div className="text-center my-3">
          <Spinner animation="border" variant="primary" />
        </div>
      )}

      {!hasNext && maintenances.length > 0 && (
        <div className="text-center text-muted py-3">
          Đã tải hết danh sách công việc bảo trì
        </div>
      )}
    </div>
  );
};

export default MaintenanceTechnicianList;