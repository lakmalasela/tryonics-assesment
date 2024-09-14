import React, { useEffect, useState } from "react";
import { Formik, Field, ErrorMessage, Form } from "formik";
import * as Yup from "yup";
import axios from "axios";
import { Button, Alert, Container } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from 'react-router-dom';
import PromotionTable from "../components/PromotionTable";
import NavComponent from "../components/NavComponent";

// Validation schema Yup
const validationSchema = Yup.object({
  startdate: Yup.date().required("Start date is required"),
  enddate: Yup.date().required("End date is required"),
  banner: Yup.mixed().required("Banner is required"),
});

const Promotion = () => {
  const navigate = useNavigate();
  const [userId, setUserId] = useState(null);
  const [username, setUsername] = useState(localStorage.getItem('username'));
  const [alertVisible, setAlertVisible] = useState(false); 
  const [status, setStatus] = useState(null); 

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/users/${username}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        setUserId(response.data.id);
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    if (username) {
      fetchUser();
    }
  }, [username]);

  const handleSubmit = async (values, { resetForm }) => {
    if (!userId) {
      setStatus({ success: false, message: "User ID is not available" });
      return;
    }

    const formData = new FormData();
    formData.append("startdate", values.startdate);
    formData.append("enddate", values.enddate);
    formData.append("banner", values.banner);
    formData.append("userId", userId);

    const token = localStorage.getItem("token");

    try {
      const response = await axios.post("http://localhost:8080/promotions/createpromotion", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          "Authorization": `Bearer ${token}`,
        },
      });

      setStatus({ success: true, message: "Promotion created successfully" });
      resetForm();
      setAlertVisible(true);
      setTimeout(() => setAlertVisible(false), 5000); // Hide alert after 5 seconds
    } catch (error) {
      setStatus({ success: false, message: error.response ? error.response.data : error.message });
      setAlertVisible(true);
    }
  };

  return (
    <Container fluid style={{ width: '100%', margin: '0 auto' }}>
      <NavComponent />
      <div className="row">
        <div className="col-md-4">
          <h3 style={{ marginTop: '50px', marginBottom: '20px' }}>Create Promotion</h3>
        </div>
      </div>

      {status && alertVisible && (
        <Alert variant={status.success ? "success" : "danger"}>
          {status.message}
        </Alert>
      )}

      <Formik
        initialValues={{ startdate: "", enddate: "", banner: null }}
        validationSchema={validationSchema}
        onSubmit={handleSubmit}
      >
        {({ setFieldValue }) => (
          <Form className="mb-4">
            <div className="form-group">
              <label htmlFor="startdate">Start Date</label>
              <Field name="startdate" type="date" className="form-control" id="startdate" />
              <ErrorMessage name="startdate" component="div" className="text-danger" />
            </div>

            <div className="form-group">
              <label htmlFor="enddate">End Date</label>
              <Field name="enddate" type="date" className="form-control" id="enddate" />
              <ErrorMessage name="enddate" component="div" className="text-danger" />
            </div>

            <div className="form-group">
              <label htmlFor="banner">Banner</label>
              <input
                name="banner"
                type="file"
                className="form-control"
                id="banner"
                onChange={(event) => setFieldValue("banner", event.currentTarget.files[0])}
              />
              <ErrorMessage name="banner" component="div" className="text-danger" />
            </div>

            <Button type="submit" variant="primary" className="mt-3">
              Create Promotion
            </Button>
          </Form>
        )}
      </Formik>

      <PromotionTable />
    </Container>
  );
};

export default Promotion;
