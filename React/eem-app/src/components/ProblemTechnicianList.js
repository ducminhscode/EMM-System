import { useContext, useEffect, useState, useCallback } from "react";
import { Card, Button, Spinner, Modal, Alert, Form } from "react-bootstrap";
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

  if (loading) return <div className="text-center mt-4"><Spinner animation="border" /></div>;
  if (error) return <div className="text-danger text-center mt-4">{error}</div>;
  if (problems.length === 0 && !loading)
    return <div className="text-center mt-4">Không có lỗi nào được giao</div>;

  return (
    <div className="container mt-4">
      <h4 className="mb-4">Danh sách lỗi được giao</h4>

      {problems.map((p) => (
        <Card key={p.id} className="mb-3 shadow-sm">
          <Card.Body>
            <Card.Title>{p.deviceName}</Card.Title>
            <Card.Text>
              <strong>Mô tả:</strong> {p.description}<br />
              <strong>Ngày xảy ra:</strong>{" "}
              {new Date(p.happenedDate).toLocaleDateString()}<br />
              <strong>Mức độ:</strong> {p.fatalLevel}<br />
              <strong>Trạng thái:</strong> {p.problemStatus}
            </Card.Text>

            <Button
              variant={p.isDone ? "secondary" : "primary"}
              onClick={() => handleOpenUpdateModal(p.id)}
            >
              {p.isDone ? "Cập nhật sửa chữa" : "Sửa chữa"}
            </Button>
            <Card.Text> {p.isDone ? "Bạn đã hoàn thành sửa chữa, chờ kĩ thuật viên còn lại" : ""}</Card.Text>
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
        <div className="text-center my-3">
          <Spinner animation="border" variant="primary" />
        </div>
      )}

    </div>
  );
};

export default ProblemTechnicianList;