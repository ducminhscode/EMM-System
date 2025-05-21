import { useEffect, useState } from "react";
import { Form, Button, Alert, Spinner } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";

const ProblemTechnician = () => {
  const { problemId } = useParams();
  const navigate = useNavigate();
  const [expense, setExpense] = useState("");
  const [description, setDescription] = useState("");
  const [repairTypeId, setRepairTypeId] = useState("");
  const [repairTypes, setRepairTypes] = useState([]);
  const [loadingRepairTypes, setLoadingRepairTypes] = useState(true);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRepairTypes = async () => {
      try {
        const res = await authApis().get(endpoints["repairType"]);
        setRepairTypes(res.data);
      } catch (err) {
        console.error("Lỗi khi lấy danh sách Repair Type:", err);
        setError("Không thể tải loại sửa chữa");
      } finally {
        setLoadingRepairTypes(false);
      }
    };

    fetchRepairTypes();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);

    try {
      const formData = new FormData();
      formData.append("expense", expense);
      formData.append("description", description);
      formData.append("repairTypeId", repairTypeId);

      const response = await authApis().patch(
        `${endpoints["updateProblemTechnicianId"]}${problemId}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setMessage(response.data);
      setTimeout(() => {
        navigate("/problem-technician-list", { state: { refresh: true } });
      }, 1000);
    } catch (err) {
      setError(err.response?.data || "Lỗi không xác định");
    }
  };

  return (
    <div className="container mt-4">
      <Form
        onSubmit={handleSubmit}
        className="p-3 border rounded shadow-sm bg-light"
      >
        <h5>Cập nhật sửa chữa cho vấn đề #{problemId}</h5>

        {message && <Alert variant="success">{message}</Alert>}
        {error && <Alert variant="danger">{error}</Alert>}

        <Form.Group className="mb-3">
          <Form.Label>Chi phí</Form.Label>
          <Form.Control
            type="number"
            min="0"
            value={expense}
            onChange={(e) => setExpense(e.target.value)}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Loại sửa chữa</Form.Label>
          {loadingRepairTypes ? (
            <Spinner animation="border" size="sm" />
          ) : (
            <Form.Select
              value={repairTypeId}
              onChange={(e) => setRepairTypeId(e.target.value)}
              required
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
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Cập nhật sửa chữa
        </Button>
      </Form>
    </div>
  );
};

export default ProblemTechnician;