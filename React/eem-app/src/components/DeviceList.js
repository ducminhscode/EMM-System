import React, { useState, useEffect, useRef, useCallback } from 'react';
import { authApis, endpoints } from '../configs/Apis';
import {
    Button,
    Container,
    Row,
    Col,
    Card,
    Spinner,
    Alert,
    Form
} from 'react-bootstrap';

const DevicesList = () => {
    const [devices, setDevices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const observer = useRef();

    const formatDate = (timestamp) => {
        if (!timestamp) return 'N/A';
        const date = new Date(timestamp);
        return date.toLocaleDateString('vi-VN', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
        });
    };

    const lastDeviceElementRef = useCallback(
        (node) => {
            if (loading || !hasMore) return;
            if (observer.current) observer.current.disconnect();
            observer.current = new IntersectionObserver((entries) => {
                if (entries[0].isIntersecting) {
                    setPage((prevPage) => prevPage + 1);
                }
            });
            if (node) observer.current.observe(node);
        },
        [loading, hasMore]
    );

    useEffect(() => {
        const fetchDevices = async () => {
            if (!hasMore) return;
            setLoading(true);
            try {
                const response = await authApis().get(`${endpoints["devices"]}?page=${page}`);
                const newDevices = response.data;

                if (newDevices.length === 0) {
                    setHasMore(false);
                } else {
                    setDevices((prevDevices) => {
                        const existingIds = new Set(prevDevices.map((d) => d.id));
                        const filteredNewDevices = newDevices.filter((d) => !existingIds.has(d.id));
                        return [...prevDevices, ...filteredNewDevices];
                    });
                }
            } catch (error) {
                console.error('Error fetching devices:', error);
                setError('Không thể tải danh sách thiết bị');
            } finally {
                setLoading(false);
            }
        };

        fetchDevices();
    }, [page]);

    const handleReportIssue = (deviceId) => {
        console.log(`Báo cáo sự cố cho thiết bị ID: ${deviceId}`);
    };

    if (loading && devices.length === 0)
        return (
            <div className="d-flex justify-content-center align-items-center" style={{ height: '200px' }}>
                <Spinner animation="border" variant="primary" />
            </div>
        );

    if (error)
        return (
            <Container className="mt-4">
                <Alert variant="danger">{error}</Alert>
            </Container>
        );

    if (devices.length === 0)
        return (
            <Container className="text-center mt-5">
                <p className="text-muted">Không có thiết bị nào</p>
            </Container>
        );

    return (
        <Container className="py-5">
            <Row className="align-items-center mb-4">
                <Col>
                    <h3 className="text-3xl font-bold text-gray-900 tracking-tight">Danh sách thiết bị</h3>
                </Col>
                <Col md="6">
                    <div className="position-relative">
                        <Form.Control
                            type="text"
                            placeholder="Tìm kiếm thiết bị..."
                            className="rounded-pill px-4 py-2 shadow-sm border border-secondary"
                        />
                        <svg className="w-5 h-5 absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                            </svg>
                    </div>
                </Col>
            </Row>

            {devices.map((device, index) => (
                <Card
                    key={device.id}
                    className="mb-4 shadow-sm border-0 rounded-4"
                    ref={devices.length === index + 1 ? lastDeviceElementRef : null}
                >
                    <Card.Body className="p-4">
                        <Row>
                            <Col md={8}>
                                <h5 className="fw-semibold text-dark mb-1">{device.name}</h5>
                                <p className="text-muted mb-3">{device.manufacturer}</p>
                                <Row className="mb-2">
                                    <Col sm={6}><strong>Mã thiết bị:</strong>{device.id}</Col>
                                    <Col sm={6}><strong>Ngày mua:</strong> {formatDate(device.purchaseDate)}</Col>
                                </Row>
                                <Row>
                                    <Col sm={6}><strong>Ngày tạo:</strong> {formatDate(device.createdDate)}</Col>
                                    <Col sm={6}><strong>Cập nhật:</strong> {formatDate(device.updatedDate)}</Col>
                                </Row>
                            </Col>
                            <Col md={4} className="d-flex flex-column justify-content-center align-items-end gap-2 mt-3 mt-md-0">
                                <Button
                                    variant="danger"
                                    size="sm"
                                    className="rounded-pill px-3 w-50"
                                    onClick={() => handleReportIssue(device.id)}
                                >
                                    Báo sự cố
                                </Button>
                                <Button
                                    variant="outline-primary"
                                    size="sm"
                                    className="rounded-pill px-3 w-50"
                                    onClick={() => console.log(`Xem chi tiết thiết bị ID: ${device.id}`)}
                                >
                                    Chi tiết
                                </Button>
                            </Col>
                        </Row>
                    </Card.Body>
                </Card>
            ))}

            {loading && (
                <div className="text-center my-4">
                    <Spinner animation="border" variant="primary" />
                </div>
            )}
        </Container>
    );
};

export default DevicesList;
