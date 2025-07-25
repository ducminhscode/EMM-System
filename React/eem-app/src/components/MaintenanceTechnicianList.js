import { useContext, useEffect, useState, useCallback } from "react";
import { Card, Button, Spinner, Modal, Alert, Form, Container, Row, Col } from "react-bootstrap";
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
  const [searchQuery, setSearchQuery] = useState('');
  const [debouncedQuery, setDebouncedQuery] = useState(searchQuery);
  const [filteredMaintenances, setFilteredMaintenances] = useState([]);
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

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedQuery(searchQuery);
    }, 700);
    return () => clearTimeout(handler);
  }, [searchQuery]);

  useEffect(() => {
    if (debouncedQuery.trim() === '') {
      setFilteredMaintenances(maintenances);
    } else {
      const filtered = maintenances.filter((m) =>
        m.deviceName?.toLowerCase().includes(debouncedQuery.toLowerCase())
      );
      setFilteredMaintenances(filtered);
    }
  }, [maintenances, debouncedQuery]);

  if (loading)
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '200px' }}>
        <Spinner animation="border" variant="primary" />
      </div>
    );
  if (error)
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  if (filteredMaintenances.length === 0 && !loading)
    return (
      <Container className="py-5">
        <Row className="align-items-center mb-4">
          <Col>
            <h3 className="text-3xl font-bold text-gray-900 tracking-tight">Danh sách công việc bảo trì được giao</h3>
          </Col>
          <Col md="6">
            <div className="position-relative">
              <Form.Control
                type="text"
                placeholder="Tìm kiếm theo tên thiết bị..."
                className="rounded-pill px-4 py-2 shadow-sm border border-secondary"
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
              />
              <svg
                className="w-5 h-5 absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                ></path>
              </svg>
            </div>
          </Col>
        </Row>
        <p className="text-center mt-5">
          {searchQuery ? 'Không tìm thấy công việc bảo trì phù hợp' : 'Không có công việc bảo trì nào được giao'}
        </p>
      </Container>
    );

  const today = new Date();

  return (
    <Container className="py-5">
      <Row className="align-items-center mb-4">
        <Col>
          <h3 className="text-3xl font-bold text-gray-900 tracking-tight">Danh sách công việc bảo trì được giao</h3>
        </Col>
        <Col md="6">
          <div className="position-relative">
            <Form.Control
              type="text"
              placeholder="Tìm kiếm theo tên thiết bị..."
              className="rounded-pill px-4 py-2 shadow-sm border border-secondary"
              value={searchQuery}
              onChange={e => setSearchQuery(e.target.value)}
            />
            <svg
              className="w-5 h-5 absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              ></path>
            </svg>
          </div>
        </Col>
      </Row>

      {filteredMaintenances.map((m, idx) => {
        const startDate = new Date(m.startDate);
        const isPastDue = startDate > today;

        return (
          <Card
            key={m.id}
            className="mb-4 shadow-sm border rounded-4"
          >
            <Card.Body className="p-4">
              <Row>
                <Col md={8}>
                  <h5 className="fw-semibold text-dark mb-1">{m.deviceName}</h5>
                  <p className="text-muted mb-3">{m.title}</p>
                  <Row className="mb-2">
                    <Col sm={6}><strong>Bắt đầu:</strong> {new Date(m.startDate).toLocaleDateString()}</Col>
                    <Col sm={6}><strong>Kết thúc:</strong> {new Date(m.endDate).toLocaleDateString()}</Col>
                  </Row>
                  <Row>
                    <Col sm={6}><strong>Trạng thái:</strong> {m.maintenanceStatus}</Col>
                    <Col sm={6}></Col>
                  </Row>
                </Col>
                <Col md={4} className="d-flex flex-column justify-content-center align-items-end gap-2 mt-3 mt-md-0">
                  {!isPastDue && m.isCap && m.maintenanceStatus === "Chưa bảo trì" ? (
                    <Button
                      variant="success"
                      onClick={() => handleOpenConfirmModal(m.id)}
                      className="rounded-pill px-3 w-75"
                    >
                      Xác nhận bảo trì
                    </Button>
                  ) : (
                    !isPastDue && (
                      <Button
                        variant={m.maintenanceStatus === "Đang bảo trì" ? "primary" : "secondary"}
                        onClick={() => handleOpenUpdateModal(m.id)}
                        hidden={!m.isCap}
                        className="rounded-pill px-3 w-75"
                      >
                        Cập nhật bảo trì
                      </Button>
                    )
                  )}
                </Col>
              </Row>
            </Card.Body>
          </Card>
        );
      })}

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
        <div className="text-center my-4">
          <Spinner animation="border" variant="primary" />
        </div>
      )}
    </Container>
  );
};

export default MaintenanceTechnicianList;