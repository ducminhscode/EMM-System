import { useContext } from "react";
import { Button, Container, Image, Nav, Navbar } from "react-bootstrap";
import { MyDispatchContext, MyUserContext } from "../../configs/Contexts";
import { useNavigate } from "react-router-dom";
import cookie from "react-cookies";
import NotificationBell from "../NotificationBell";

const Header = () => {
  const dispatch = useContext(MyDispatchContext);
  const nav = useNavigate();
  const user = useContext(MyUserContext);

  const handleLogout = () => {
    cookie.remove("token", { path: "/" });
    dispatch({ type: "logout" });
    nav("/login");
  };

  const navigateToProfile = () => {
    nav("/profile");
  };

  return (
    <Navbar expand="lg" className="bg-body-tertiary rounded-3 shadow-sm">
      <Container fluid>
        <Navbar.Brand
          href="/"
          className="text-2xl font-bold text-blue-600 transition-colors duration-300 tracking-wide me-5"
        >
          Equipment Maintenance Management
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: "100px" }}
            navbarScroll
          >

            <Nav.Link href="#action2" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-4">Thiết bị</Nav.Link>
            <Nav.Link href="/chat"className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-4">Trò chuyện</Nav.Link>
            <Nav.Link href="/problem-technician-list" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-4">Sự cố</Nav.Link>
            <Nav.Link href="/maintenance-technician-list" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-4">Bảo trì</Nav.Link>
          </Nav>
          <Nav className="ms-auto align-items-center">
            {user && (
              <>
                <NotificationBell />
                <div
                  className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 ms-3"
                  style={{
                    cursor: "pointer",
                    transition: "all 0.2s ease-in-out",
                  }}
                  onClick={navigateToProfile}
                >
                  <div className="position-relative me-2">
                    <Image
                      src={user.avatar || "/default-avatar.png"}
                      roundedCircle
                      width="40"
                      height="40"
                      className="object-fit-cover border border-2 border-primary"
                      alt="Avatar"
                      style={{
                        minWidth: "40px",
                        minHeight: "40px",
                        transition: "transform 0.2s",
                      }}
                    />
                  </div>
                  <div className="d-flex flex-column me-3">
                    <span className="fw-semibold text-dark">
                      {user.firstName} {user.lastName}
                    </span>
                  </div>
                </div>
              </>
            )}
            <Button
              variant="outline-danger"
              onClick={handleLogout}
              className="ms-2"
              size="sm"
              style={{
                transition: "all 0.2s",
              }}
            >
              Đăng xuất
            </Button>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

<<<<<<< HEAD
    useEffect(() => {
        const fetchProblems = async () => {
            if (user && user.userRole === "ROLE_TECHNICIAN") {
                try {
                    const response = await authApis().get(`${endpoints['problemTechnicianId']}${user.id}`);
                    setProblems(response.data || []);
                } catch (err) {
                    console.error("Lỗi khi lấy danh sách problems:", err);
                }
            }
        };
        fetchProblems();
    }, [user]);



    const getBadgeVariant = (level) => {
        switch (level?.toLowerCase()) {
            case "cao":
                return "danger";    // đỏ
            case "trung bình":
                return "warning";   // cam
            case "thấp":
                return "info";      // xanh dương
            default:
                return "secondary"; // xám
        }
    };

    const notificationPopover = (
        <Popover id="notification-popover" className="shadow-lg rounded-3 border-0">
            <Popover.Header as="h5" className="bg-primary text-white rounded-top">🔔 Thông báo lỗi</Popover.Header>
            <Popover.Body style={{ maxHeight: '300px', overflowY: 'auto' }}>
                {problems.length === 0 ? (
                    <div className="text-center text-muted">Không có lỗi nào</div>
                ) : (
                    <ul className="list-unstyled mb-0">
                        {problems.map((p, index) => (
                            <li key={index} className="mb-3 pb-2 border-bottom">
                                <div className="fw-semibold text-dark">
                                    <span className="text-primary">Thiết bị: {p.deviceName}</span>
                                </div>
                                <div className="text-body mb-1">Mô tả: {p.description}</div>
                                <div className="d-flex justify-content-between align-items-center">
                                    <span className={`badge bg-${getBadgeVariant(p.fatalLevel)} px-2 py-1`}>
                                        Mức độ: {p.fatalLevel}
                                    </span>
                                    <small className="text-muted fst-italic">
                                        {new Date(p.happenedDate).toLocaleString()}
                                    </small>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </Popover.Body>
        </Popover>
    );


    return (
        <>
            <Navbar expand="lg" className="bg-body-tertiary rounded-3 shadow-sm">
                <Container fluid>
                    <Navbar.Brand href="/" className="text-2xl font-bold text-blue-600 transition-colors duration-300 tracking-wide me-5"
                    >Equipment Maintenance Management</Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarScroll" />
                    <Navbar.Collapse id="navbarScroll">
                        <Nav
                            className="me-auto my-2 my-lg-0"
                            style={{ maxHeight: '100px' }}
                            navbarScroll
                        >
                            <Nav.Link href="/devices" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-4">Thiết bị</Nav.Link>
                            <Nav.Link href="/chat" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1">Trò chuyện</Nav.Link>
                            <Nav.Link href="/problem-technician-list" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1">Sự cố</Nav.Link>
                        </Nav>
                        <Nav className="ms-auto align-items-center">
                            {user && (
                                <>
                                    <OverlayTrigger
                                        trigger="click"
                                        placement="bottom"
                                        overlay={notificationPopover}
                                        rootClose
                                    >
                                        <div className="position-relative me-3" style={{ cursor: "pointer" }}>
                                            <BiBell size={24} />
                                            {problems.length > 0 && (
                                                <Badge bg="danger" pill className="position-absolute top-0 start-100 translate-middle">
                                                    {problems.length}
                                                </Badge>
                                            )}
                                        </div>
                                    </OverlayTrigger>

                                    <div
                                        className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1"
                                        style={{
                                            cursor: 'pointer',
                                            transition: 'all 0.2s ease-in-out'
                                        }}
                                        onClick={navigateToProfile}
                                    >
                                        <div className="position-relative me-2">
                                            <Image
                                                src={user.avatar || '/default-avatar.png'}
                                                roundedCircle
                                                width="40"
                                                height="40"
                                                className="object-fit-cover border border-2 border-primary"
                                                alt="Avatar"
                                                style={{
                                                    minWidth: '40px',
                                                    minHeight: '40px',
                                                    transition: 'transform 0.2s'
                                                }}
                                            />
                                        </div>
                                        <div className="d-flex flex-column me-3">
                                            <span className="fw-semibold text-dark">{user.firstName} {user.lastName}</span>
                                        </div>
                                    </div>
                                </>
                            )}
                            <Button
                                variant="outline-danger"
                                onClick={handleLogout}
                                className="ms-2"
                                size="sm"
                                style={{
                                    transition: 'all 0.2s'
                                }}
                            >
                                Đăng xuất
                            </Button>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </>
    );
}
=======
>>>>>>> 8bb2270cbfe47cca34b5260b5172306d22bb585d
export default Header;