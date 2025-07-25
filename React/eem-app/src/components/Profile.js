import { useContext, useEffect, useState } from "react";
import { Alert, Button, Card, Container, Form, Image, Row, Col, Spinner } from "react-bootstrap";
import { MyDispatchContext, MyUserContext } from "../configs/Contexts";
import { authApis, endpoints } from "../configs/Apis";
import { FaCamera } from "react-icons/fa";

const Profile = () => {
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatchContext);

    const [profile, setProfile] = useState({
        firstName: "",
        lastName: "",
        email: "",
        phone: "",
        password: "",
        confirmPassword: "",
        currentPassword: "",
    });
    const [avatar, setAvatar] = useState(null);
    const [previewAvatar, setPreviewAvatar] = useState("");
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ text: "", variant: "" });
    const [activeTab, setActiveTab] = useState("profile");

    useEffect(() => {
        if (user) {
            setProfile({
                firstName: user.firstName || "",
                lastName: user.lastName || "",
                email: user.email || "",
                phone: user.phone || "",
                password: "",
                confirmPassword: "",
                currentPassword: "",
            });
            setPreviewAvatar(user.avatar);
        }
    }, [user]);

    useEffect(() => {
        return () => {
            if (previewAvatar && previewAvatar.startsWith("blob:")) {
                URL.revokeObjectURL(previewAvatar);
            }
        };
    }, [previewAvatar]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProfile((prev) => ({ ...prev, [name]: value }));
    };

    const handleAvatarChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            if (!file.type.startsWith("image/")) {
                setMessage({ text: "Vui lòng chọn file hình ảnh", variant: "danger" });
                return;
            }
            if (file.size > 5 * 1024 * 1024) {
                setMessage({ text: "File ảnh quá lớn (tối đa 5MB)", variant: "danger" });
                return;
            }
            setAvatar(file);
            setPreviewAvatar(URL.createObjectURL(file));
        }
    };

    const validateEmail = (email) => {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    };

    const validatePhone = (phone) => {
        return /^[0-9]{10}$/.test(phone);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage({ text: "", variant: "" });

        if (!validateEmail(profile.email)) {
            setMessage({ text: "Email không hợp lệ", variant: "danger" });
            setLoading(false);
            return;
        }
        if (!validatePhone(profile.phone)) {
            setMessage({ text: "Số điện thoại không hợp lệ (phải có 10 chữ số)", variant: "danger" });
            setLoading(false);
            return;
        }

        try {
            const formData = new FormData();
            formData.append("firstName", profile.firstName);
            formData.append("lastName", profile.lastName);
            formData.append("email", profile.email);
            formData.append("phone", profile.phone);
            if (avatar) formData.append("avatar", avatar);

            const res = await authApis().put(endpoints["updateProfile"], formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            dispatch({
                type: "login",
                payload: res.data,
            });

            setMessage({ text: "Cập nhật thông tin thành công", variant: "success" });
            setAvatar(null);
        } catch (error) {
            setMessage({
                text: error.response?.data?.errorMessage || "Có lỗi xảy ra khi cập nhật thông tin",
                variant: "danger",
            });
        } finally {
            setLoading(false);
        }
    };

    const handlePasswordChange = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage({ text: "", variant: "" });

        if (profile.password.length < 6) {
            setMessage({ text: "Mật khẩu mới phải có ít nhất 6 ký tự", variant: "danger" });
            setLoading(false);
            return;
        }
        if (profile.password !== profile.confirmPassword) {
            setMessage({ text: "Mật khẩu và xác nhận mật khẩu không khớp", variant: "danger" });
            setLoading(false);
            return;
        }

        try {
            await authApis().post(endpoints["changePassword"], {
                currentPassword: profile.currentPassword,
                newPassword: profile.password,
            });

            setMessage({ text: "Đổi mật khẩu thành công", variant: "success" });
            setProfile((prev) => ({
                ...prev,
                password: "",
                confirmPassword: "",
                currentPassword: "",
            }));
        } catch (error) {
            console.error("Change password error:", error);
            setMessage({
                text: error.response?.data?.message || "Có lỗi xảy ra khi đổi mật khẩu",
                variant: "danger",
            });
        } finally {
            setLoading(false);
        }
    };

    const renderRoleBadge = () => {
        switch (user?.userRole) {
            case "ROLE_ADMIN":
                return (
                    <span className="badge bg-danger">
                        Quản trị viên
                    </span>
                );
            case "ROLE_TECHNICIAN":
                return (
                    <span className="badge bg-primary">
                        Kỹ thuật viên
                    </span>
                );
            case "ROLE_EMPLOYEE":
                return (
                    <span className="badge bg-success">
                        Nhân viên
                    </span>
                );
            default:
                return <span className="badge bg-secondary">Khác</span>;
        }
    };

    return (
        <>
            <Container className="py-4">
                <Card className="border-0 shadow-sm overflow-hidden">
                    <div className="border-bottom mt-2">
                        <div className="nav nav-tabs">
                            <button
                                className={`nav-link ${activeTab === "profile" ? "active fw-bold text-primary" : ""}`}
                                onClick={() => setActiveTab("profile")}
                            >
                                Thông tin cá nhân
                            </button>
                            <button
                                className={`nav-link ${activeTab === "password" ? "active fw-bold text-primary" : ""}`}
                                onClick={() => setActiveTab("password")}
                            >
                                Đổi mật khẩu
                            </button>
                        </div>
                    </div>

                    <Card.Body className="p-4">
                        {message.text && (
                            <Alert
                                variant={message.variant}
                                className="mb-4"
                                onClose={() => setMessage({ text: "", variant: "" })}
                                dismissible
                            >
                                {message.text}
                            </Alert>
                        )}

                        {activeTab === "profile" ? (
                            <Form onSubmit={handleSubmit}>
                                <Row className="mb-4 align-items-center">
                                    <Col xs="auto">
                                        <div className="position-relative">
                                            <Image
                                                src={previewAvatar}
                                                roundedCircle
                                                width={100}
                                                height={100}
                                                className="w-32 h-32 border border-3 border-primary object-fit-cover"
                                                alt="Avatar"
                                            />
                                            <label
                                                htmlFor="avatar-upload"
                                                className="position-absolute bottom-0 end-0 bg-primary text-white rounded-circle p-2 cursor-pointer"
                                            >
                                                <FaCamera />
                                                <input
                                                    id="avatar-upload"
                                                    type="file"
                                                    accept="image/*"
                                                    onChange={handleAvatarChange}
                                                    className="d-none"
                                                />
                                            </label>
                                        </div>
                                    </Col>
                                    <Col>
                                        <h3 className="h5 fw-bold mb-1">
                                            {user?.firstName} {user?.lastName}
                                        </h3>
                                        <div className="mb-2">{renderRoleBadge()}</div>
                                        <div className="text-muted small">
                                            <span className="d-block">Tên đăng nhập: {user?.username}</span>
                                        </div>
                                    </Col>
                                </Row>

                                <Row className="g-3">
                                    <Col md={6}>
                                        <Form.Group controlId="firstName">
                                            <Form.Label>
                                                Họ <span className="text-danger">*</span>
                                            </Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="firstName"
                                                value={profile.firstName}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group controlId="lastName">
                                            <Form.Label>
                                                Tên <span className="text-danger">*</span>
                                            </Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="lastName"
                                                value={profile.lastName}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group controlId="email">
                                            <Form.Label>
                                                Email <span className="text-danger">*</span>
                                            </Form.Label>
                                            <Form.Control
                                                type="email"
                                                name="email"
                                                value={profile.email}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group controlId="phone">
                                            <Form.Label>
                                                Số điện thoại <span className="text-danger">*</span>
                                            </Form.Label>
                                            <Form.Control
                                                type="tel"
                                                name="phone"
                                                value={profile.phone}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <div className="mt-4">
                                    <Button
                                        variant="primary"
                                        type="submit"
                                        disabled={loading}
                                        className="w-100 py-2"
                                    >
                                        {loading ? (
                                            <>
                                                <Spinner animation="border" size="sm" className="me-2" />
                                                Đang cập nhật...
                                            </>
                                        ) : (
                                            "Cập nhật thông tin"
                                        )}
                                    </Button>
                                </div>
                            </Form>
                        ) : (
                            <Form onSubmit={handlePasswordChange}>
                                <div className="mb-4">
                                    <Form.Group controlId="currentPassword">
                                        <Form.Label>
                                            Mật khẩu hiện tại <span className="text-danger">*</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="password"
                                            name="currentPassword"
                                            value={profile.currentPassword}
                                            onChange={handleChange}
                                            required
                                        />
                                    </Form.Group>
                                </div>

                                <Row className="g-3 mb-4">
                                    <Col md={6}>
                                        <Form.Group controlId="password">
                                            <Form.Label>
                                                Mật khẩu mới <span className="text-danger">*</span>
                                            </Form.Label>
                                            <Form.Control
                                                type="password"
                                                name="password"
                                                value={profile.password}
                                                onChange={handleChange}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group controlId="confirmPassword">
                                            <Form.Label>
                                                Xác nhận mật khẩu <span className="text-danger">*</span>
                                            </Form.Label>
                                            <Form.Control
                                                type="password"
                                                name="confirmPassword"
                                                value={profile.confirmPassword}
                                                onChange={handleChange}
                                                required
                                                isInvalid={
                                                    profile.password !== profile.confirmPassword &&
                                                    profile.confirmPassword !== ""
                                                }
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                Mật khẩu không khớp
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <div className="mt-4">
                                    <Button
                                        variant="primary"
                                        type="submit"
                                        disabled={loading}
                                        className="w-100 py-2"
                                    >
                                        {loading ? (
                                            <>
                                                <Spinner animation="border" size="sm" className="me-2" />
                                                Đang xử lý...
                                            </>
                                        ) : (
                                            "Đổi mật khẩu"
                                        )}
                                    </Button>
                                </div>
                            </Form>
                        )}
                    </Card.Body>
                </Card>
            </Container>
        </>
    );
};

export default Profile;