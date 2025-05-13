import { Container, Row, Col, Button, Card } from "react-bootstrap";
import { FaTools, FaChartLine, FaBell, FaCalendarCheck } from "react-icons/fa";

const Home = () => {
  return (
    <div style={{ backgroundColor: "#f8f9fa", minHeight: "100vh", marginTop: "30px" }}>
      {/* Hero Banner */}
      <div
        style={{
          background: "linear-gradient(to right, #007bff, #0056b3)",
          color: "#ffffff",
          padding: "5rem 2rem",
          textAlign: "center",
          borderBottom: "4px solid #00ccff",
        }}
      >
        <Container>
          <h1 style={{ fontSize: "2.5rem", fontWeight: 700, marginBottom: "1rem" }}>
            Equipment Maintenance Management
          </h1>
          <p style={{ fontSize: "1.2rem", maxWidth: "600px", margin: "0 auto 2rem" }}>
            Optimize your equipment performance with our cutting-edge maintenance management system.
          </p>
          <Button
            variant="light"
            size="lg"
            href="#"
            style={{
              fontWeight: 600,
              padding: "0.75rem 2rem",
              border: "2px solid #ffffff",
              transition: "all 0.3s ease",
            }}
            onMouseOver={(e) => (e.target.style.backgroundColor = "#e0e0e0")}
            onMouseOut={(e) => (e.target.style.backgroundColor = "#ffffff")}
          >
            Get Started
          </Button>
        </Container>
      </div>

      {/* Giới thiệu */}
      <Container style={{ padding: "3rem 2rem" }}>
        <Row className="align-items-center">
          <Col md={6}>
            <h2 style={{ fontSize: "2rem", fontWeight: 600, color: "#1a1a1a" }}>
              Streamline Your Maintenance Process
            </h2>
            <p style={{ fontSize: "1rem", color: "#6c757d", lineHeight: 1.6, marginTop: "1rem" }}>
              Our platform helps businesses manage equipment maintenance efficiently, reduce downtime,
              and extend asset lifespan with real-time tracking and predictive analytics.
            </p>
          </Col>
          <Col md={6}>
            <div
              style={{
                backgroundColor: "#007bff",
                height: "200px",
                borderRadius: "10px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                color: "#ffffff",
                fontSize: "1.2rem",
                fontWeight: 500,
              }}
            >
              Placeholder for Equipment Image
            </div>
          </Col>
        </Row>
      </Container>

      {/* Tính năng nổi bật */}
      <div style={{ backgroundColor: "#ffffff", padding: "3rem 2rem" }}>
        <Container>
          <h2 style={{ fontSize: "2rem", fontWeight: 600, textAlign: "center", marginBottom: "2rem", color: "#1a1a1a" }}>
            Key Features
          </h2>
          <Row>
            <Col md={3} sm={6} style={{ marginBottom: "1.5rem" }}>
              <Card
                style={{
                  border: "none",
                  boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                  borderRadius: "10px",
                  height: "100%",
                }}
              >
                <Card.Body style={{ textAlign: "center", padding: "2rem" }}>
                  <FaTools size={40} style={{ color: "#007bff", marginBottom: "1rem" }} />
                  <Card.Title style={{ fontSize: "1.25rem", fontWeight: 600, color: "#1a1a1a" }}>
                    Maintenance Scheduling
                  </Card.Title>
                  <Card.Text style={{ fontSize: "0.9rem", color: "#6c757d" }}>
                    Plan and automate maintenance tasks with ease.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={3} sm={6} style={{ marginBottom: "1.5rem" }}>
              <Card
                style={{
                  border: "none",
                  boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                  borderRadius: "10px",
                  height: "100%",
                }}
              >
                <Card.Body style={{ textAlign: "center", padding: "2rem" }}>
                  <FaChartLine size={40} style={{ color: "#007bff", marginBottom: "1rem" }} />
                  <Card.Title style={{ fontSize: "1.25rem", fontWeight: 600, color: "#1a1a1a" }}>
                    Performance Analytics
                  </Card.Title>
                  <Card.Text style={{ fontSize: "0.9rem", color: "#6c757d" }}>
                    Gain insights with real-time performance data.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={3} sm={6} style={{ marginBottom: "1.5rem" }}>
              <Card
                style={{
                  border: "none",
                  boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                  borderRadius: "10px",
                  height: "100%",
                }}
              >
                <Card.Body style={{ textAlign: "center", padding: "2rem" }}>
                  <FaBell size={40} style={{ color: "#007bff", marginBottom: "1rem" }} />
                  <Card.Title style={{ fontSize: "1.25rem", fontWeight: 600, color: "#1a1a1a" }}>
                    Alerts & Notifications
                  </Card.Title>
                  <Card.Text style={{ fontSize: "0.9rem", color: "#6c757d" }}>
                    Stay informed with timely maintenance alerts.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            <Col md={3} sm={6} style={{ marginBottom: "1.5rem" }}>
              <Card
                style={{
                  border: "none",
                  boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                  borderRadius: "10px",
                  height: "100%",
                }}
              >
                <Card.Body style={{ textAlign: "center", padding: "2rem" }}>
                  <FaCalendarCheck size={40} style={{ color: "#007bff", marginBottom: "1rem" }} />
                  <Card.Title style={{ fontSize: "1.25rem", fontWeight: 600, color: "#1a1a1a" }}>
                    Asset Tracking
                  </Card.Title>
                  <Card.Text style={{ fontSize: "0.9rem", color: "#6c757d" }}>
                    Monitor and manage all your assets in one place.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>

      {/* Call-to-Action */}
      <div
        style={{
          backgroundColor: "#007bff",
          color: "#ffffff",
          padding: "3rem 2rem",
          textAlign: "center",
        }}
      >
        <Container>
          <h2 style={{ fontSize: "2rem", fontWeight: 600, marginBottom: "1rem" }}>
            Ready to Optimize Your Maintenance?
          </h2>
          <Button
            variant="light"
            size="lg"
            href="#"
            style={{
              fontWeight: 600,
              padding: "0.75rem 2rem",
              border: "2px solid #ffffff",
              transition: "all 0.3s ease",
            }}
            onMouseOver={(e) => (e.target.style.backgroundColor = "#e0e0e0")}
            onMouseOut={(e) => (e.target.style.backgroundColor = "#ffffff")}
          >
            Sign Up Now
          </Button>
        </Container>
      </div>
    </div>
  );
};

export default Home;