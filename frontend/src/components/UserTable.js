import React, { useState, useEffect } from 'react';
import { Table, Button, Pagination, Modal, Form, Alert } from 'react-bootstrap';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

const UserTable = () => {
    const [users, setUsers] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [showEditModal, setShowEditModal] = useState(false);
    const [editUser, setEditUser] = useState(null);
    const [status, setStatus] = useState(null);
    const [token] = useState(localStorage.getItem('token'));  

    const pageSize = 5;  // Define users per page

    // Fetch users with pagination
    const fetchUsers = async (page) => {
        try {
            const response = await axios.get(`http://localhost:8080/users?page=${page}&size=${pageSize}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            console.log('Response:', response.data); 
            setUsers(response.data.users);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Error fetching users:', error.response ? error.response.data : error.message);
            setStatus({ success: false, message: error.response ? error.response.data : error.message });
        }
    };
    

    // Delete user
    const handleDelete = async (userId) => {
        try {
            await axios.delete(`http://localhost:8080/user/${userId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setStatus({ success: true, message: 'User deleted successfully' });
            fetchUsers(currentPage); 
        } catch (error) {
            setStatus({ success: false, message: 'Failed to delete user' });
        }
    };

    // Update user
    const handleUpdateUser = async (user) => {
        try {
            await axios.put(`http://localhost:8080/user/${user.id}`, {
                username: user.username,
                password: user.password
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            setStatus({ success: true, message: 'User updated successfully' });
            setShowEditModal(false);
            setEditUser(null); 
            fetchUsers(currentPage); 
        } catch (error) {
            setStatus({ success: false, message: 'Failed to update user' });
        }
    };

    // Open edit modal
    const handleEdit = (user) => {
        setEditUser(user);
        setShowEditModal(true);
    };

    // Handle page change
    const handlePageChange = (page) => {
        setCurrentPage(page);
        fetchUsers(page);
    };

    useEffect(() => {
        fetchUsers(currentPage);
    }, [currentPage]);

    return (
        <div className="container mt-5">
            <h2>User Management</h2>
            {status && (
                <Alert variant={status.success ? 'success' : 'danger'}>
                    {status.message}
                </Alert>
            )}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Date Added</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.username}</td>
                            <td>{user.addedate}</td>
                            <td>
                                <Button variant="warning" onClick={() => handleEdit(user)}>Edit</Button>{' '}
                                <Button variant="danger" onClick={() => handleDelete(user.id)}>Delete</Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            {/* Pagination */}
            <Pagination>
                {[...Array(totalPages)].map((_, i) => (
                    <Pagination.Item
                        key={i + 1}
                        active={i + 1 === currentPage}
                        onClick={() => handlePageChange(i + 1)}
                    >
                        {i + 1}
                    </Pagination.Item>
                ))}
            </Pagination>

            {/* Edit Modal */}
            <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit User</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {editUser && (
                        <Form>
                            <Form.Group controlId="formUsername">
                                <Form.Label>Username</Form.Label>
                                <Form.Control
                                    type="text"
                                    defaultValue={editUser.username}
                                    onChange={(e) => setEditUser({ ...editUser, username: e.target.value })}
                                />
                            </Form.Group>
                            <Form.Group controlId="formPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Enter new password"
                                    onChange={(e) => setEditUser({ ...editUser, password: e.target.value })}
                                />
                            </Form.Group>
                            <Button
                                variant="primary"
                                onClick={() => handleUpdateUser(editUser)}
                            >
                                Save Changes
                            </Button>
                        </Form>
                    )}
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default UserTable;
