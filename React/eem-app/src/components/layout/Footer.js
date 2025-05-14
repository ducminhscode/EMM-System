import { Container, Row, Col, Nav, Alert } from "react-bootstrap";
import { FaFacebook, FaTwitter, FaInstagram, FaEnvelope } from "react-icons/fa";

const Footer = () => {
  return (
    <footer style={{ backgroundColor: '#ffffff', color: '#ffffff', padding: '2rem 0', fontFamily: 'Arial, sans-serif' }}>
      <Container className="bg-body-tertiary rounded-3 shadow-sm">
        <Row>
          <Col md={4} sm={12} style={{ marginBottom: '1rem', marginTop: '1rem' }}>
            <h5 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1rem', color: '#1a1a1a' }}>
              Equipment Maintenance Management
            </h5>
            <p style={{ color: '#b0b0b0', fontSize: '0.9rem' }}>
              Chào mừng bạn đến với Equipment Maintenance Management - nơi mang đến trải nghiệm tuyệt vời nhất!
            </p>
          </Col>

          <Col md={4} sm={12} style={{ marginBottom: '1rem', marginTop: '1rem'  }}>
            <h5 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1rem', color: '#1a1a1a' }}>
              Liên kết nhanh
            </h5>
            <Nav className="flex-column">
              <Nav.Link
                href="#"
                style={{ color: '#b0b0b0', padding: 0, fontSize: '0.9rem' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#b0b0b0')}
              >
                Về chúng tôi
              </Nav.Link>
              <Nav.Link
                href="#"
                style={{ color: '#b0b0b0', padding: 0, fontSize: '0.9rem' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#b0b0b0')}
              >
                Sản phẩm
              </Nav.Link>
              <Nav.Link
                href="#"
                style={{ color: '#b0b0b0', padding: 0, fontSize: '0.9rem' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#b0b0b0')}
              >
                Liên hệ
              </Nav.Link>
              <Nav.Link
                href="#"
                style={{ color: '#b0b0b0', padding: 0, fontSize: '0.9rem' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#b0b0b0')}
              >
                FAQ
              </Nav.Link>
            </Nav>
          </Col>

          <Col md={4} sm={12} style={{ marginBottom: '1rem',marginTop: '1rem'  }}>
            <h5 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1rem', color: '#1a1a1a' }}>
              Liên hệ
            </h5>
            <p style={{ color: '#b0b0b0', fontSize: '0.9rem', display: 'flex', alignItems: 'center' }}>
              <FaEnvelope style={{ marginRight: '0.5rem' }} /> support@ousecommerce.com
            </p>
            <div style={{ display: 'flex', gap: '1rem' }}>
              <div
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: '#1a1a1a' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#1a1a1a')}
              >
                <FaFacebook size={24} />
              </div>
              <div
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: '#1a1a1a' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#1a1a1a')}
              >
                <FaTwitter size={24} />
              </div>
              <div
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: '#1a1a1a' }}
                onMouseOver={(e) => (e.target.style.color = '#00ccff')}
                onMouseOut={(e) => (e.target.style.color = '#1a1a1a')}
              >
                <FaInstagram size={24} />
              </div>
            </div>
          </Col>
        </Row>
      </Container>
      <Row style={{ marginTop: '1.5rem' }}>
        <Col style={{ textAlign: 'center' }}>
          <Alert
            variant="dark"
            style={{
              backgroundColor: '#ffffff',
              color: '#1a1a1a',
              border: 'none',
              fontSize: '0.9rem',
              marginBottom: 0,
            }}
          >
            Equipment Maintenance Management © {new Date().getFullYear()}. All Rights Reserved.
          </Alert>
        </Col>
      </Row>
    </footer>
  );
};

export default Footer;