import { useEffect, useState } from "react";
import { Form, Button, Alert} from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";

const MaintenanceTechnician = () => {
  const { maintenanceId } = useParams();
  const navigate = useNavigate();
  const [expenseLast, setExpenseLast] = useState("");
  const [description, setDescription] = useState("");
  const [, setLoading] = useState(true);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(false);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
    setError(null);

    try {
      const formData = new FormData();
      formData.append("expenseLast", expenseLast);
      formData.append("description", description);

      const response = await authApis().patch(
        `${endpoints["updateMaintenanceTechnicianId"]}${maintenanceId}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      setMessage(response.data);
      setTimeout(() => {
        navigate("/maintenance-technician-list", { state: { refresh: true } });
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
        <h5>Cập nhật bảo trì cho công việc {maintenanceId}</h5>

        {message && <Alert variant="success">{message}</Alert>}
        {error && <Alert variant="danger">{error}</Alert>}

        <Form.Group className="mb-3">
          <Form.Label>Chi phí cuối</Form.Label>
          <Form.Control
            type="number"
            min="0"
            value={expenseLast}
            onChange={(e) => setExpenseLast(e.target.value)}
            required
          />
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
          Cập nhật bảo trì
        </Button>
      </Form>
    </div>
  );
};

export default MaintenanceTechnician;