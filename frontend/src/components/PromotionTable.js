import React, { useState, useEffect } from "react";
import { Table, Button, Pagination, Modal, Form, Alert } from "react-bootstrap";
import axios from "axios";
import { Formik, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import "bootstrap/dist/css/bootstrap.min.css";

// Validation schema
const validationSchema = Yup.object({
  startdate: Yup.date().required("Start date is required"),
  enddate: Yup.date().required("End date is required"),
});

const PromotionTable = () => {
  const [promotions, setPromotions] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [showEditModal, setShowEditModal] = useState(false);
  const [editPromotion, setEditPromotion] = useState(null);
  const [status, setStatus] = useState(null);
  const [token] = useState(localStorage.getItem("token"));

  const pageSize = 5; // Define promotions per page

  // Fetch promotions with pagination
  const fetchPromotions = async (page) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/promotions?page=${page}&size=${pageSize}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setPromotions(response.data.promotions || []);
      setCurrentPage(response.data.currentPage || 1);
      setTotalPages(response.data.totalPages || 1);
    } catch (error) {
      setStatus({
        success: false,
        message: error.response ? error.response.data : error.message,
      });
    }
  };

  // Delete promotion
  const handleDelete = async (promotionId) => {
    try {
      await axios.delete(`http://localhost:8080/promotions/${promotionId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setStatus({ success: true, message: "Promotion deleted successfully" });
      fetchPromotions(currentPage); // Refresh promotions after deletion
    } catch (error) {
      setStatus({ success: false, message: "Failed to delete promotion" });
    }
  };

  // Update promotion
  const handleUpdatePromotion = async (promotion) => {
    try {
      const formData = new FormData();
      formData.append("startdate", promotion.startdate);
      formData.append("enddate", promotion.enddate);

      // Append the new file 
      if (promotion.bannerFile) {
        formData.append("banner", promotion.bannerFile);
      } else if (editPromotion && editPromotion.banner) {
        // If no new file, append the existing  data
        formData.append("banner", editPromotion.banner); // This might be a URL or data
      }

      await axios.put(
        `http://localhost:8080/promotions/${promotion.id}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setStatus({ success: true, message: "Promotion updated successfully" });
      setShowEditModal(false);
      setEditPromotion(null); // Clear the form after successful update
      fetchPromotions(currentPage); // Refresh promotions after updating
    } catch (error) {
      setStatus({ success: false, message: "Failed to update promotion" });
    }
  };

  // Open edit modal
  const handleEdit = (promotion) => {
    setEditPromotion(promotion);
    setShowEditModal(true);
  };

  // Handle page change
  const handlePageChange = (page) => {
    setCurrentPage(page);
    fetchPromotions(page);
  };

  useEffect(() => {
    fetchPromotions(currentPage);
  }, [currentPage]);

  return (
    <div className="container mt-5">
      <h2>Promotion Management</h2>
      {status && (
        <Alert variant={status.success ? "success" : "danger"}>
          {status.message}
        </Alert>
      )}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Banner</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {promotions.length > 0 ? (
            promotions.map((promotion) => (
              <tr key={promotion.id}>
                <td>{promotion.id}</td>
                <td>{new Date(promotion.startdate).toLocaleDateString()}</td>
                <td>{new Date(promotion.enddate).toLocaleDateString()}</td>
                <td>
                  {promotion.banner && (
                    <img
                      src={`data:image/jpeg;base64,${promotion.banner}`}
                      alt="Promotion Banner"
                      style={{
                        width: "100px",
                        height: "auto",
                        objectFit: "cover",
                      }}
                    />
                  )}
                </td>
                <td>
                  <Button
                    variant="warning"
                    onClick={() => handleEdit(promotion)}
                  >
                    Edit
                  </Button>{" "}
                  <Button
                    variant="danger"
                    onClick={() => handleDelete(promotion.id)}
                  >
                    Delete
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" className="text-center">
                No promotions found
              </td>
            </tr>
          )}
        </tbody>
      </Table>

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

      <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Promotion</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {editPromotion && (
            <Formik
              initialValues={{
                startdate: editPromotion.startdate
                  ? editPromotion.startdate.substring(0, 10)
                  : "",
                enddate: editPromotion.enddate
                  ? editPromotion.enddate.substring(0, 10)
                  : "",
                bannerFile: null, 
              }}
              validationSchema={validationSchema}
              enableReinitialize={true}
              onSubmit={async (values) => {
                await handleUpdatePromotion({
                  ...editPromotion,
                  startdate: values.startdate,
                  enddate: values.enddate,
                  bannerFile: values.bannerFile,
                });
              }}
            >
              {({ handleSubmit, setFieldValue, values }) => (
                <Form noValidate onSubmit={handleSubmit}>
                  <Form.Group>
                    <Form.Label>Start Date</Form.Label>
                    <Field
                      type="date"
                      name="startdate"
                      className="form-control"
                    />
                    <ErrorMessage
                      name="startdate"
                      component="div"
                      className="text-danger"
                    />
                  </Form.Group>

                  <Form.Group>
                    <Form.Label>End Date</Form.Label>
                    <Field
                      type="date"
                      name="enddate"
                      className="form-control"
                    />
                    <ErrorMessage
                      name="enddate"
                      component="div"
                      className="text-danger"
                    />
                  </Form.Group>

                  <Form.Group>
                    <Form.Label>Banner</Form.Label>
                    {/* Display the  image */}
                    {editPromotion.banner && (
                      <div>
                        <img
                          src={`data:image/jpeg;base64,${editPromotion.banner}`}
                          alt="Current Banner"
                          style={{
                            width: "100%",
                            maxHeight: "200px",
                            marginBottom: "10px",
                            objectFit: "contain",
                          }}
                        />
                      </div>
                    )}
                    <input
                      type="file"
                      name="bannerFile"
                      className="form-control"
                      onChange={(e) => setFieldValue("bannerFile", e.target.files[0])}
                    />
                    <ErrorMessage
                      name="bannerFile"
                      component="div"
                      className="text-danger"
                    />
                  </Form.Group>

                  <Button variant="primary" type="submit">
                    Save Changes
                  </Button>
                </Form>
              )}
            </Formik>
          )}
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default PromotionTable;
