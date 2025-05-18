import { useContext, useEffect, useState } from "react";
import { Card, Button, Spinner, Collapse } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import ProblemTechnician from "./ProblemTechnician";
import { MyUserContext } from "../configs/Contexts";

const ProblemTechnicianList = ({}) => {
    const [problems, setProblems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedProblem, setSelectedProblem] = useState(null);
    const [showRepairForm, setShowRepairForm] = useState({});
    const user = useContext(MyUserContext);

    useEffect(() => {
        const fetchProblems = async () => {
            try {
                const res = await authApis().get(`${endpoints['problemTechnicianId']}${user.id}`);
                setProblems(res.data || []);
            } catch (err) {
                console.error("Lỗi khi lấy danh sách problems:", err);
                setError("Không thể tải danh sách lỗi");
            } finally {
                setLoading(false);
            }
        };
        fetchProblems();
    }, [user]);

    const handleShowRepairForm = (problemId) => {
        setShowRepairForm((prev) => ({
            ...prev,
            [problemId]: !prev[problemId],
        }));
        setSelectedProblem(problemId);
    };

    const handleSuccess = () => {
        window.location.reload();
    };

    if (loading) return <Spinner animation="border" />;
    if (error) return <div className="text-danger">{error}</div>;
    if (problems.length === 0) return <div>Không có lỗi nào được giao</div>;

    return (
        <div className="container mt-4">
            <h4>Danh sách lỗi được giao</h4>
            {problems.map((p) => (
                <Card key={p.id} className="mb-3 shadow-sm">
                    <Card.Body>
                        <Card.Title>{p.deviceName}</Card.Title>
                        <Card.Text>
                            <strong>Mô tả:</strong> {p.description} <br />
                            <strong>Ngày xảy ra:</strong> {new Date(p.happenedDate).toLocaleDateString()} <br />
                            <strong>Mức độ nghiêm trọng:</strong> {p.severityLevel}
                        </Card.Text>
                        <Button
                            variant="success"
                            onClick={() => handleShowRepairForm(p.id)}
                            aria-expanded={showRepairForm[p.id]}
                        >
                            {showRepairForm[p.id] ? "Đóng form sửa chữa" : "Xác nhận sửa chữa"}
                        </Button>

                        <Collapse in={showRepairForm[p.id]}>
                            <div className="mt-3">
                                <ProblemTechnician problemId={p.id} onSuccess={handleSuccess} />
                            </div>
                        </Collapse>
                    </Card.Body>
                </Card>
            ))}
        </div>
    );
};

export default ProblemTechnicianList;
