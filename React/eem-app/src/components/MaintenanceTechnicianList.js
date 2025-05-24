import { useContext, useEffect, useState, useCallback } from "react";
import { Card, Button, Spinner, Modal, Alert, Form } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext} from "../configs/Contexts";

const MaintenanceTechnicianList = () => {
  const [maintenances, setMaintenances] = useState([]);
  const [page, setPage] = useState(1);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(true);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [error, setError] = useState(null);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [selectedMaintenanceId, setSelectedMaintenanceId] = useState(null);
  const [confirmModalMessage, setConfirmModalMessage] = useState(null);
  const [confirmModalError, setConfirmModalError] = useState(null);
  const [updateModalMessage, setUpdateModalMessage] = useState(null);
  const [updateModalError, setUpdateModalError] = useState(null);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [updateSubmitting, setUpdateSubmitting] = useState(false);
  const [expenseLast, setExpenseLast] = useState("");
  const [description, setDescription] = useState("");
  const user = useContext(MyUserContext);
  const location = useLocation();
  const navigate = useNavigate();

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

  const refreshMaintenances = () => {
    setMaintenances([]);
    setPage(1);
    setHasNext(true);
    setLoading(true);
    loadMaintenances(1);
  };

  const handleConfirmMaintenance = async () => {
    setConfirmLoading(true);
    setConfirmModalMessage(null);
    setConfirmModalError(null);

    try {
      const response = await authApis().patch(
        `${endpoints["updateMaintenanceStatus"]}${selectedMaintenanceId}`
      );
      setConfirmModalMessage(response.data);
      setTimeout(() => {
        setShowConfirmModal(false);
        setSelectedMaintenanceId(null);
        setConfirmModalMessage(null);
        setConfirmModalError(null);
        refreshMaintenances();
      }, 1000);
    } catch (err) {
      setConfirmModalError(err.response?.data || "Lỗi không xác định");
    } finally {
      setConfirmLoading(false);
    }
  };

  const handleUpdateMaintenance = async () => {
    if (!expenseLast) {
      setUpdateModalError("Vui lòng nhập chi phí thực tế");
      return;
    }

    setUpdateSubmitting(true);
    setUpdateModalMessage(null);
    setUpdateModalError(null);

    try {
      const formData = new FormData();
      formData.append("expenseLast", expenseLast);
      formData.append("description", description);

      const response = await authApis().patch(
        `${endpoints["updateMaintenanceTechnicianId"]}${selectedMaintenanceId}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setUpdateModalMessage(response.data);
      setTimeout(() => {
        setShowUpdateModal(false);
        setSelectedMaintenanceId(null);
        setUpdateModalMessage(null);
        setUpdateModalError(null);
        setExpenseLast("");
        setDescription("");
        refreshMaintenances();
        navigate(location.pathname, { state: { refresh: Date.now() } });
      }, 1000);
    } catch (err) {
      setUpdateModalError(err.response?.data || "Lỗi không xác định");
    } finally {
      setUpdateSubmitting(false);
    }
  };

  const handleOpenConfirmModal = (maintenanceId) => {
    setSelectedMaintenanceId(maintenanceId);
    setShowConfirmModal(true);
  };

  const handleOpenUpdateModal = (maintenanceId) => {
    setSelectedMaintenanceId(maintenanceId);
    setExpenseLast("");
    setDescription("");
    setShowUpdateModal(true);
  };

  const handleCloseConfirmModal = () => {
    setShowConfirmModal(false);
    setSelectedMaintenanceId(null);
    setConfirmModalMessage(null);
    setConfirmModalError(null);
  };

  const handleCloseUpdateModal = () => {
    setShowUpdateModal(false);
    setSelectedMaintenanceId(null);
    setUpdateModalMessage(null);
    setUpdateModalError(null);
    setExpenseLast("");
    setDescription("");
  };

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

  if (loading) return <div className="text-center mt-4"><Spinner animation="border" /></div>;
  if (error) return <div className="text-danger text-center mt-4">{error}</div>;
  if (maintenances.length === 0 && !loading)
    return <div className="text-center mt-4">Không có công việc bảo trì nào được giao</div>;

  const today = new Date();

  return (
    <div className="container mt-4">
      <h4 className="mb-4">Danh sách công việc bảo trì được giao</h4>

      {maintenances.map((m) => {
        const startDate = new Date(m.startDate);
        const isPastDue = startDate > today;

        return (
          <Card key={m.id} className="mb-3 shadow-sm">
            <Card.Body>
              <Card.Title>{m.deviceName}</Card.Title>
              <Card.Text>
                <strong>Tiêu đề:</strong> {m.title}<br />
                <strong>Bắt đầu:</strong> {new Date(m.startDate).toLocaleDateString()}<br />
                <strong>Kết thúc:</strong> {new Date(m.endDate).toLocaleDateString()}<br />
                <strong>Trạng thái:</strong> {m.maintenanceStatus}
              </Card.Text>

              {!isPastDue && m.isCap && m.maintenanceStatus === "Chưa bảo trì" ? (
                <Button
                  variant="success"
                  onClick={() => handleOpenConfirmModal(m.id)}
                >
                  Xác nhận bảo trì
                </Button>
              ) : (
                !isPastDue && (
                  <Button
                    variant={m.maintenanceStatus === "Đang bảo trì" ? "primary" : "secondary"}
                    onClick={() => handleOpenUpdateModal(m.id)}
                    hidden={!m.isCap}
                  >
                    Cập nhật bảo trì
                  </Button>
                )
              )}
            </Card.Body>
          </Card>
        );
      })}

      {/* Confirm Maintenance Modal */}
      <Modal show={showConfirmModal} onHide={handleCloseConfirmModal}>
        <Modal.Header closeButton>
          <Modal.Title>Xác nhận bảo trì</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {confirmModalMessage && <Alert variant="success">{confirmModalMessage}</Alert>}
          {confirmModalError && <Alert variant="danger">{confirmModalError}</Alert>}
          {!confirmModalMessage && !confirmModalError && (
            <p>Bạn có chắc chắn muốn xác nhận bảo trì cho công việc này?</p>
          )}
        </Modal.Body>
        <Modal.Footer>
          {!confirmModalMessage && (
            <>
              <Button variant="secondary" onClick={handleCloseConfirmModal} disabled={confirmLoading}>
                Hủy
              </Button>
              <Button
                variant="primary"
                onClick={handleConfirmMaintenance}
                disabled={confirmLoading}
              >
                {confirmLoading ? <Spinner animation="border" size="sm" /> : "Xác nhận"}
              </Button>
            </>
          )}
        </Modal.Footer>
      </Modal>

      {/* Update Maintenance Modal */}
      <Modal show={showUpdateModal} onHide={handleCloseUpdateModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Cập nhật bảo trì</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {updateModalMessage ? (
            <Alert variant="success">{updateModalMessage}</Alert>
          ) : (
            <>
              {updateModalError && <Alert variant="danger">{updateModalError}</Alert>}
              <Form>
                <Form.Group className="mb-3">
                  <Form.Label>Chi phí thực tế <span className="text-danger">*</span></Form.Label>
                  <Form.Control
                    type="number"
                    min="0"
                    value={expenseLast}
                    onChange={(e) => setExpenseLast(e.target.value)}
                    required
                    disabled={updateSubmitting}
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Mô tả</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    disabled={updateSubmitting}
                    placeholder="Mô tả chi tiết bảo trì..."
                  />
                </Form.Group>
              </Form>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          {!updateModalMessage && (
            <>
              <Button variant="secondary" onClick={handleCloseUpdateModal} disabled={updateSubmitting}>
                Hủy
              </Button>
              <Button
                variant="primary"
                onClick={handleUpdateMaintenance}
                disabled={updateSubmitting}
              >
                {updateSubmitting ? (
                  <Spinner as="span" size="sm" animation="border" role="status" />
                ) : "Cập nhật"}
              </Button>
            </>
          )}
        </Modal.Footer>
      </Modal>

      {isLoadingMore && (
        <div className="text-center my-3">
          <Spinner animation="border" variant="primary" />
        </div>
      )}
    </div>
  );
};

export default MaintenanceTechnicianList;