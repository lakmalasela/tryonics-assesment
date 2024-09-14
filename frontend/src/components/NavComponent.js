
import { useNavigate } from 'react-router-dom';
import { Button, Container, Row, Col,Navbar,Nav } from 'react-bootstrap';

const NavComponent = ()=>{

    const navigate = useNavigate();

    const handleLogout = () => {
      localStorage.removeItem('token');  
      navigate('/login');  
    };

    return(

        
        <Navbar bg="primary" variant="dark" expand="lg">
        <Navbar.Brand href="#">
            <Nav.Link onClick={() => navigate('/dashboard')}>Dashboard</Nav.Link>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link onClick={() => navigate('/createuser')}>Users</Nav.Link>
            <Nav.Link onClick={() => navigate('/promotion')}>Promotions</Nav.Link>
          </Nav>
          <Button variant="outline-light" onClick={handleLogout}>
            Logout
          </Button>
        </Navbar.Collapse>
      </Navbar>
    )

}

export default NavComponent;