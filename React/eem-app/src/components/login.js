import { useContext, useEffect, useState } from 'react';
import { Alert, Button, Card, Col, Container, FloatingLabel, Form, Row } from 'react-bootstrap';
import MySpinner from './layout/MySpinner';
import { useLocation, useNavigate } from 'react-router-dom';
import cookie from 'react-cookies';
import { MyDispatchContext } from '../configs/Contexts';
import Apis, { authApis, endpoints } from '../configs/Apis';

const Login = () => {
  const info = [
    {
      label: 'Tên đăng nhập',
      field: 'username',
      type: 'text',
    },
    {
      label: 'Mật khẩu',
      field: 'password',
      type: 'password',
    },
  ];

  const [user, setUser] = useState({});
  const [msg, setMsg] = useState();
  const [loading, setLoading] = useState(false);
  const [fieldErrors, setFieldErrors] = useState({});

  const dispatch = useContext(MyDispatchContext);
  const nav = useNavigate();
  const location = useLocation();

  const setState = (value, field) => {
    setUser({ ...user, [field]: value });
  };

  const validate = () => {
    for (let i of info) {
      if (!(i.field in user) || user[i.field] === '') {
        setFieldErrors({ [i.field]: `${i.label} không được để trống` });
        return false;
      }
    }
    return true;
  };

  const login = async (e) => {
    e.preventDefault();
    setMsg('');
    setFieldErrors({});

    if (validate() === true) {
      try {
        setLoading(true);
        let res = await Apis.post(endpoints['login'], {
          ...user,
        });
        cookie.save('token', res.data.token);

        let u = await authApis().get(endpoints['profile']);

        dispatch({
          type: 'login',
          payload: u.data,
        });
        nav('/');
      } catch (ex) {
        console.error('Login error:', ex);

        if (ex.response?.status === 401) {
          setMsg('Sai tên đăng nhập hoặc mật khẩu');
        } else if (ex.message) {
          setMsg(ex.message);
        } else {
          setMsg('Lỗi hệ thống hoặc kết nối. Vui lòng thử lại sau.');
        }
      } finally {
        setLoading(false);
      }
    }
  };

  useEffect(() => {
    const checkToken = async () => {
      const token = cookie.load('token');
      if (token) {
        try {
          setLoading(true);
          let userData = await authApis().get(endpoints['profile']);
          dispatch({
            type: 'login',
            payload: userData.data,
          });
          const from = location.state?.from?.pathname || '/';
          nav(from, { replace: true });
        } catch (ex) {
          console.error('Token validation error:', ex);
          cookie.remove('token', { path: '/' });
        } finally {
          setLoading(false);
        }
      }
    };

    checkToken();
  }, [dispatch, nav, location]);

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Row className="w-75">
        <Col md={{ span: 12, offset: 0 }}>
          <Row className="align-items-center">
            <Col md={6}>
              <div>
                <p className="text-primary display-2">
                  <strong>EEM</strong>
                </p>
                <h4>Hệ thống quản lý bảo trì thiết bị</h4>
              </div>
            </Col>
            <Col md={6}>
              <Card className="shadow rounded-4">
                <Card.Body>
                  <h3 className="text-center text-primary mt-1 mb-4">Đăng nhập</h3>

                  {msg && (
                    <Alert variant="danger" className="mb-4">
                      {msg}
                    </Alert>
                  )}

                  <Form onSubmit={login}>
                    {info.map((f) => (
                      <Form.Group key={f.field} className="mb-4">
                        <FloatingLabel controlId={`floating-${f.field}`} label={f.label}>
                          <Form.Control
                            type={f.type}
                            placeholder={f.label}
                            required
                            value={user[f.field] || ''}
                            onChange={(e) => setState(e.target.value, f.field)}
                            isInvalid={!!fieldErrors[f.field]}
                          />
                          <Form.Control.Feedback type="invalid">
                            {fieldErrors[f.field]}
                          </Form.Control.Feedback>
                        </FloatingLabel>
                      </Form.Group>
                    ))}

                    <div className="d-flex justify-content-between align-items-center mb-4">
                      <Form.Check
                        id="remember-me"
                        label="Ghi nhớ đăng nhập"
                        className="text-sm text-gray-900"
                      />
                      <a
                        href="#forgot-password"
                        className="text-sm font-medium text-primary hover-text-primary"
                      >
                        Quên mật khẩu?
                      </a>
                    </div>

                    <div>
                      {loading ? (
                        <MySpinner />
                      ) : (
                        <Button
                          type="submit"
                          variant="primary"
                          className="w-100 py-2"
                        >
                          Đăng nhập
                        </Button>
                      )}
                    </div>
                  </Form>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;