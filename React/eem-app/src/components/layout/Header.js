import { useContext} from "react";
import { Button, Container, Image, Nav, Navbar} from "react-bootstrap";
import { MyDispatchContext, MyUserContext } from "../../configs/Contexts";
import { useNavigate } from "react-router-dom";
import cookie from 'react-cookies';
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
              <Nav.Link href="/devices" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-3">Thiết bị</Nav.Link>
              <Nav.Link href="/chat" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-3">Trò chuyện</Nav.Link>
              <Nav.Link href="/problem-technician-list" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-3">Sự cố</Nav.Link>
              <Nav.Link href="/maintenance-technician-list" className="d-flex align-items-center rounded-lg hover:bg-gray-300 transition-all cursor-pointer p-1 me-3">Bảo trì</Nav.Link>
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
export default Header;