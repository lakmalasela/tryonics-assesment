import React from 'react';
import { Container} from 'react-bootstrap';
import NavComponent from '../components/NavComponent';


const Dashboard = () => {


  return (
    <Container fluid style={{ width: '100%', margin: '0 auto' }}>
        <NavComponent/>
        <div>
          <p>Welcome to Dashboard</p>
        </div>
    </Container>
  );
};

export default Dashboard;
