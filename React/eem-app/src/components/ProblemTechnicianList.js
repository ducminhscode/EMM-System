import { useContext, useEffect, useState, useCallback } from "react";
import { Card, Button, Spinner } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom"; // Add useLocation
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Contexts";

const ProblemTechnicianList = () => {
  const [problems, setProblems] = useState([]);
  const [page, setPage] = useState(1);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(true);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [error, setError] = useState(null);
  const user = useContext(MyUserContext);
  const navigate = useNavigate();
  const location = useLocation(); // Add useLocation

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
    }
  }, [user, loadProblems, location.state?.refresh]); // Add location.state?.refresh

  useEffect(() => {
    if (page > 1) {
      loadProblems(page);
    }
  }, [page, loadProblems]);

  const handleNavigateToRepair = (problemId) => {
    navigate(`/problem-technician/${problemId}`);
  };

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
              <strong>Mức độ:</strong> {p.fatalLevel}
            </Card.Text>

            <Button
              variant={p.isDone ? "secondary" : "success"}
              onClick={() => handleNavigateToRepair(p.id)}
            >
              {p.isDone ? "Đã sửa chữa, cập nhật lại" : "Xác nhận sửa chữa"}
            </Button>
          </Card.Body>
        </Card>
      ))}

      {isLoadingMore && (
        <div className="text-center my-3">
          <Spinner animation="border" variant="primary" />
        </div>
      )}

      {!hasNext && problems.length > 0 && (
        <div className="text-center text-muted py-3">
          Đã tải hết danh sách lỗi
        </div>
      )}
    </div>
  );
};

export default ProblemTechnicianList;