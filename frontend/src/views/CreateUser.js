import React from 'react';
import { Container, Form as BootstrapForm, Button, Alert } from 'react-bootstrap';
import { Formik, Field, Form, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import UserTable from '../components/UserTable';
import { useNavigate } from 'react-router-dom';
import NavComponent from '../components/NavComponent';

// Validation schema with Yup
const validationSchema = Yup.object().shape({
    username: Yup.string()
        .required('Username is required'),
    password: Yup.string()
        .min(6, 'Password must be at least 6 characters')
        .required('Password is required'),
});

const CreateUser = () => {

    // const navigate = useNavigate();

    // const handleLogout = () => {
    //   localStorage.removeItem('token');  // Remove token from localStorage
    //   navigate('/login');  // Redirect to login
    // };
    const handleSubmit = async (values, { setSubmitting, setStatus }) => {
        const token = localStorage.getItem('token');  
    
        try {
            const response = await axios.post(
                'http://localhost:8080/createuser',
                values,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`, 
                        'Content-Type': 'application/json'
                    }
                }
            );
            setStatus({ success: true, message: response.data });
        } catch (error) {
            if (error.response && error.response.status === 409) {
                // User already exists
                setStatus({ success: false, message: 'User already exists. Please choose a different username.' });
            } else {
                // Generic error
                setStatus({ success: false, message: 'An error occurred while creating the admin user.' });
            }
        }
        setSubmitting(false);
    };
    
    return (
        <Container  fluid style={{ width: '100%',marginBottom:'5vh'}}>
            <NavComponent/>
            
            <h3 style={{marginTop:'50px',marginBottom:'20px'}}>Create User</h3>
            <Formik
                initialValues={{ username: '', password: '' }}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
            >
                {({ isSubmitting, status, errors, touched }) => (
                    <Form>
                        <BootstrapForm.Group className="mb-3">
                            <BootstrapForm.Label>Username</BootstrapForm.Label>
                            <Field
                                type="text"
                                name="username"
                                as={BootstrapForm.Control}
                                isInvalid={!!errors.username && touched.username}
                            />
                            <ErrorMessage name="username" component={BootstrapForm.Control.Feedback} type="invalid" />
                        </BootstrapForm.Group>
                        <BootstrapForm.Group className="mb-3">
                            <BootstrapForm.Label>Password</BootstrapForm.Label>
                            <Field
                                type="password"
                                name="password"
                                as={BootstrapForm.Control}
                                isInvalid={!!errors.password && touched.password}
                            />
                            <ErrorMessage name="password" component={BootstrapForm.Control.Feedback} type="invalid" />
                        </BootstrapForm.Group>
                        <Button
                            type="submit"
                            variant="primary"
                            disabled={isSubmitting}
                        >
                            Create Admin
                        </Button>
                        {status && status.message && (
                            <Alert variant={status.success ? 'success' : 'danger'} className="mt-3">
                                {status.message}
                            </Alert>
                        )}
                    </Form>
                )}
            </Formik>

             <UserTable/>
        </Container>
        
    );
};

export default CreateUser;
