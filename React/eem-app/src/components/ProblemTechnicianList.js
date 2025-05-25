import { useContext, useEffect, useState, useCallback } from "react";
import { Card, Button, Spinner, Modal, Alert, Form, Container, Row, Col } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";

const ProblemTechnicianList = () => {
  const [problems, setProblems] = useState([]);
  const [page, setPage] = useState(1);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(true);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [error, setError] = useState(null);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [selectedProblemId, setSelectedProblemId] = useState(null);
  const [updateModalMessage, setUpdateModalMessage] = useState(null);
  const [updateModalError, setUpdateModalError] = useState(null);
  const [updateSubmitting, setUpdateSubmitting] = useState(false);
  const [expense, setExpense] = useState("");
  const [description, setDescription] = useState("");
  const [repairTypeId, setRepairTypeId] = useState("");
  const [repairTypes, setRepairTypes] = useState([]);
  const [loadingRepairTypes, setLoadingRepairTypes] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [debouncedQuery, setDebouncedQuery] = useState(searchQuery);
  const [filteredProblems, setFilteredProblems] = useState([]);
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

  const loadProblems = useCallback(
    async (pageNumber) => {
      if (!user?.id) {
        setError("Không tìm thấy thông tin người dùng");
        setLoading(false);
        return;
      }

      try {
        const url = `${endpoints["problemTechnicianId"]}${user.id}`;
        const res = await authApis().get(url, {
          params: { page: pageNumber },
        });

        if (res.data.length === 0) {
          setHasNext(false);
          return;
        }

        const newData = filterDuplicates(res.data);
        setProblems((prev) => filterDuplicates([...prev, ...newData]));

        if (newData.length < 5) {
          setHasNext(false);
        }
      } catch (err) {
        console.error("Lỗi khi tải problems:", err);
        setError("Không thể tải danh sách lỗi");
        setHasNext(false);
      } finally {
        setLoading(false);
        setIsLoadingMore(false);
      }
    },
    [user, filterDuplicates]
  );

  const loadRepairTypes = useCallback(async () => {
    try {
      const res = await authApis().get(endpoints["repairType"]);
      setRepairTypes(res.data);
    } catch (err) {
      console.error("Lỗi khi lấy danh sách Repair Type:", err);
      setUpdateModalError("Không thể tải loại sửa chữa");
    } finally {
      setLoadingRepairTypes(false);
    }
  }, []);

  const refreshProblems = () => {
    setProblems([]);
    setPage(1);
    setHasNext(true);
    setLoading(true);
    loadProblems(1);
  };

  const handleUpdateRepair = async () => {
    if (!expense || !repairTypeId) {
      setUpdateModalError("Vui lòng nhập chi phí và chọn loại sửa chữa");
      return;
    }

    setUpdateSubmitting(true);
    setUpdateModalMessage(null);
    setUpdateModalError(null);

    try {
      const formData = new FormData();
      formData.append("expense", expense);
      formData.append("description", description);
      formData.append("repairTypeId", repairTypeId);

      const response = await authApis().patch(
        `${endpoints["updateProblemTechnicianId"]}${selectedProblemId}`,
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
        setSelectedProblemId(null);
        setUpdateModalMessage(null);
        setUpdateModalError(null);
        setExpense("");
        setDescription("");
        setRepairTypeId("");
        refreshProblems();
        navigate(location.pathname, { state: { refresh: Date.now() } });
      }, 1000);
    } catch (err) {
      setUpdateModalError(err.response?.data || "Lỗi không xác định");
    } finally {
      setUpdateSubmitting(false);
    }
  };

  const handleOpenUpdateModal = (problemId) => {
    setSelectedProblemId(problemId);
    setExpense("");
    setDescription("");
    setRepairTypeId("");
    setShowUpdateModal(true);
  };

  const handleCloseUpdateModal = () => {
    setShowUpdateModal(false);
    setSelectedProblemId(null);
    setUpdateModalMessage(null);
    setUpdateModalError(null);
    setExpense("");
    setDescription("");
    setRepairTypeId("");
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
      setProblems([]);
      setPage(1);
      setHasNext(true);
      setLoading(true);
      loadProblems(1);
      loadRepairTypes();
    }
  }, [user, loadProblems, loadRepairTypes, location.state?.refresh]);

  useEffect(() => {
    if (page > 1) {
      loadProblems(page);
    }
  }, [page, loadProblems]);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedQuery(searchQuery);
    }, 700);
    return () => clearTimeout(handler);
  }, [searchQuery]);

  useEffect(() => {
    if (debouncedQuery.trim() === '') {
      setFilteredProblems(problems);
    } else {
      const filtered = problems.filter((p) =>
        p.deviceName?.toLowerCase().includes(debouncedQuery.toLowerCase())
      );
      setFilteredProblems(filtered);
    }
  }, [problems, debouncedQuery]);

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
  if (filteredProblems.length === 0 && !loading)
    return (
      <Container className="py-5">
        <Row className="align-items-center mb-4">
          <Col>
            <h3 className="text-3xl font-bold text-gray-900 tracking-tight">Danh sách lỗi được giao</h3>
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
          {searchQuery ? 'Không tìm thấy lỗi phù hợp' : 'Không có lỗi nào được giao'}
        </p>
      </Container>
    );

  return (
    <Container className="py-5">
      <Row className="align-items-center mb-4">
        <Col>
          <h3 className="text-3xl font-bold text-gray-900 tracking-tight">Danh sách lỗi được giao</h3>
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

      {filteredProblems.map((p, idx) => (
        <Card
          key={p.id}
          className="mb-4 shadow-sm border rounded-4"
        >
          <Card.Body className="p-4">
            <Row>
              <Col md={8}>
                <h5 className="fw-semibold text-dark mb-1">{p.deviceName}</h5>
                <p className="text-muted mb-3">{p.description}</p>
                <Row className="mb-2">
                  <Col sm={6}><strong>Ngày xảy ra:</strong> {new Date(p.happenedDate).toLocaleDateString()}</Col>
                  <Col sm={6}><strong>Mức độ:</strong> {p.fatalLevel}</Col>
                </Row>
                <Row>
                  <Col sm={6}><strong>Trạng thái:</strong> {p.problemStatus}</Col>
                  <Col sm={6}></Col>
                </Row>
              </Col>
              <Col md={4} className="d-flex flex-column justify-content-center align-items-end gap-2 mt-3 mt-md-0">
                <Button
                  variant={p.isDone ? "secondary" : "primary"}
                  onClick={() => handleOpenUpdateModal(p.id)}
                  className="rounded-pill px-3 w-75"
                >
                  {p.isDone ? "Cập nhật sửa chữa" : "Sửa chữa"}
                </Button>
                {p.isDone && (
                  <span className="text-success small mt-2">
                    Bạn đã hoàn thành sửa chữa, chờ kĩ thuật viên còn lại
                  </span>
                )}
              </Col>
            </Row>
          </Card.Body>
        </Card>
      ))}

      {/* Update Repair Modal */}
      <Modal show={showUpdateModal} onHide={handleCloseUpdateModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Cập nhật sửa chữa</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {updateModalMessage ? (
            <Alert variant="success">{updateModalMessage}</Alert>
          ) : (
            <>
              {updateModalError && <Alert variant="danger">{updateModalError}</Alert>}
              <Form>
                <Form.Group className="mb-3">
                  <Form.Label>Chi phí <span className="text-danger">*</span></Form.Label>
                  <Form.Control
                    type="number"
                    min="0"
                    value={expense}
                    onChange={(e) => setExpense(e.target.value)}
                    required
                    disabled={updateSubmitting}
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Loại sửa chữa <span className="text-danger">*</span></Form.Label>
                  {loadingRepairTypes ? (
                    <Spinner animation="border" size="sm" />
                  ) : (
                    <Form.Select
                      value={repairTypeId}
                      onChange={(e) => setRepairTypeId(e.target.value)}
                      required
                      disabled={updateSubmitting}
                    >
                      <option value="">Chọn loại sửa chữa</option>
                      {repairTypes.map((rt) => (
                        <option key={rt.id} value={rt.id}>
                          {rt.name}
                        </option>
                      ))}
                    </Form.Select>
                  )}
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Mô tả</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    disabled={updateSubmitting}
                    placeholder="Mô tả chi tiết sửa chữa..."
                  />
                </Form.Group>
              </Form>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          {!updateModalMessage && (
            <>
              <Button
                variant="secondary"
                onClick={handleCloseUpdateModal}
                disabled={updateSubmitting}
              >
                Hủy
              </Button>
              <Button
                variant="primary"
                onClick={handleUpdateRepair}
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

export default ProblemTechnicianList;