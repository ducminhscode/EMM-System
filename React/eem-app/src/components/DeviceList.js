import React, { useState, useEffect, useRef, useCallback } from 'react';
import { authApis, endpoints } from '../configs/Apis';
import { Button, Container, Row, Col, Card, Spinner, Alert, Form, Modal } from 'react-bootstrap';

const DevicesList = () => {
    const [devices, setDevices] = useState([]);
    const [filteredDevices, setFilteredDevices] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [debouncedQuery, setDebouncedQuery] = useState(searchQuery);
    const [showReportDialog, setShowReportDialog] = useState(false);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [reportData, setReportData] = useState({
        description: '',
        fatalLevelId: '',
        happenedDate: new Date().toISOString().split('T')[0]
    });
    const [fatalLevels, setFatalLevels] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [submitError, setSubmitError] = useState('');
    const [submitSuccess, setSubmitSuccess] = useState('');
    const observer = useRef();
    const [purchaseDate, setPurchaseDate] = useState('');

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
        const handler = setTimeout(() => {
            setDebouncedQuery(searchQuery);
        }, 700);

        return () => {
            clearTimeout(handler);
        };
    }, [searchQuery]);

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
    }, [page, hasMore]);

    useEffect(() => {
        if (debouncedQuery.trim() === '') {
            setFilteredDevices(devices);
        } else {
            const filtered = devices.filter((device) =>
                device.name.toLowerCase().includes(debouncedQuery.toLowerCase())
            );
            setFilteredDevices(filtered);
        }
    }, [devices, debouncedQuery]);

    useEffect(() => {
        const fetchFatalLevels = async () => {
            try {
                const response = await authApis().get(endpoints['fatal-level']);
                setFatalLevels(response.data);
            } catch (error) {
                console.error('Error fetching fatal levels:', error);
            }
        };
        fetchFatalLevels();
    }, []);

    const handleReportClick = (device) => {
        setSelectedDevice(device);
        setPurchaseDate(device.purchaseDate);
        console.log(new Date(Number(device.purchaseDate)).toISOString().split("T")[0]);
        setShowReportDialog(true);
    };

    const handleCloseDialog = () => {
        setShowReportDialog(false);
        setSelectedDevice(null);
        setReportData({
            description: '',
            fatalLevelId: '',
            happenedDate: new Date().toISOString().split('T')[0]
        });
        setSubmitError('');
        setSubmitSuccess('');
    };

    const handleReportChange = (e) => {
        const { name, value } = e.target;
        setReportData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmitReport = async () => {
        if (!reportData.description || !reportData.fatalLevelId) {
            setSubmitError('Vui lòng điền đầy đủ thông tin bắt buộc');
            return;
        }

        try {
            setSubmitting(true);
            setSubmitError('');

            const queryParams = new URLSearchParams({
                deviceId: selectedDevice.id,
                description: reportData.description,
                fatalLevelId: reportData.fatalLevelId,
                happenedDate: reportData.happenedDate
            }).toString();

            await authApis().post(`${endpoints['problem']}?${queryParams}`);

            setSubmitSuccess('Báo cáo sự cố thành công!');
            setTimeout(() => {
                handleCloseDialog();
            }, 1500);
        } catch (error) {
            setSubmitError(error.response?.data?.message || 'Error submitting report: ' + error.message);
        } finally {
            setSubmitting(false);
        }
    };

    const handleSearch = (e) => {
        setSearchQuery(e.target.value);
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

    if (filteredDevices.length === 0)
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
                                value={searchQuery}
                                onChange={handleSearch}
                            />
                            <svg
                                className="w-5 h-5 absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                                fill="none"
                                stroke="currentColor"
                                viewBox="0 0 24 24"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                                ></path>
                            </svg>
                        </div>
                    </Col>
                </Row>
                <p className="text-center mt-5">
                    {searchQuery ? 'Không tìm thấy thiết bị phù hợp' : 'Không có thiết bị nào'}
                </p>
            </Container>
        );

    return (
        <>
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
                                value={searchQuery}
                                onChange={handleSearch}
                            />
                            <svg
                                className="w-5 h-5 absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                                fill="none"
                                stroke="currentColor"
                                viewBox="0 0 24 24"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                                ></path>
                            </svg>
                        </div>
                    </Col>
                </Row>

                {filteredDevices.map((device, index) => (
                    <Card
                        key={device.id}
                        className="mb-4 shadow-sm border rounded-4"
                        ref={filteredDevices.length === index + 1 ? lastDeviceElementRef : null}
                    >
                        <Card.Body className="p-4">
                            <Row>
                                <Col md={8}>
                                    <h5 className="fw-semibold text-dark mb-1">{device.name}</h5>
                                    <p className="text-muted mb-3">{device.manufacturer}</p>
                                    <Row className="mb-2">
                                        <Col sm={6}><strong>Mã thiết bị:</strong> {device.id}</Col>
                                        <Col sm={6}><strong>Cơ sở:</strong> {device.facilityId}</Col>
                                    </Row>
                                    <Row>
                                        <Col sm={6}><strong>Loại:</strong> {device.typeId}</Col>
                                        <Col sm={6}><strong>Trạng thái:</strong> {device.statusId}</Col>
                                    </Row>
                                </Col>
                                {device.statusId === 'Hoạt động' && (
                                    <Col md={4} className="d-flex flex-column justify-content-center align-items-end gap-2 mt-3 mt-md-0">
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            className="rounded-pill px-3 w-50"
                                            onClick={() => handleReportClick(device)}
                                        >
                                            Báo cáo sự cố
                                        </Button>
                                    </Col>
                                )}
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
            <Modal show={showReportDialog} onHide={handleCloseDialog} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Báo cáo sự cố</Modal.Title>
                </Modal.Header>
                <Modal.Body>

                    {submitSuccess ? (
                        <Alert variant="success">{submitSuccess}</Alert>
                    ) : (
                        <>
                            {submitError && <Alert variant="danger">{submitError}</Alert>}
                            {selectedDevice && (
                                <div className="mb-3">
                                    <h6>Tên thiết bị: {selectedDevice.name}</h6>
                                    <p className="text-muted mb-3">{selectedDevice.manufacturer}</p>
                                </div>
                            )}
                            <Form.Group className="mb-3">
                                <Form.Label>Mức độ nghiêm trọng <span className="text-danger">*</span></Form.Label>
                                <Form.Select
                                    name="fatalLevelId"
                                    value={reportData.fatalLevelId}
                                    onChange={handleReportChange}
                                    disabled={submitting}
                                    required
                                >
                                    <option value="">Chọn mức độ</option>
                                    {fatalLevels.map(level => (
                                        <option key={level.id}
                                            value={level.id}>{level.name}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Ngày xảy ra sự cố <span className="text-danger">*</span></Form.Label>
                                <Form.Control
                                    type="date"
                                    name="happenedDate"
                                    value={reportData.happenedDate}
                                    onChange={handleReportChange}
                                    disabled={submitting}
                                    required
                                    max={new Date().toISOString().split("T")[0]}
                                    min={
                                        purchaseDate
                                            ? new Date(Number(purchaseDate)).toISOString().split("T")[0]
                                            : ""
                                    } />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Mô tả sự cố</Form.Label>
                                <Form.Control
                                    as="textarea"
                                    rows={3}
                                    name="description"
                                    value={reportData.description}
                                    onChange={handleReportChange}
                                    disabled={submitting}
                                    placeholder="Mô tả chi tiết sự cố..."
                                />
                            </Form.Group>
                        </>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    {!submitSuccess && (
                        <>
                            <Button variant="secondary" onClick={handleCloseDialog} disabled={submitting}>
                                Hủy
                            </Button>
                            <Button
                                variant="danger"
                                onClick={handleSubmitReport}
                                disabled={submitting}
                            >
                                {submitting ? (
                                    <Spinner as="span" size="sm" animation="border" role="status" />
                                ) : 'Gửi báo cáo'}
                            </Button>
                        </>
                    )}
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default DevicesList;