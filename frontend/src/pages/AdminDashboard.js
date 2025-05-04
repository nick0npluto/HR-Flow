import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function AdminDashboardPage() {
  const navigate = useNavigate();
  const [employees, setEmployees] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [updateForm, setUpdateForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    salary: '',
    ssn: '',
    dateOfBirth: ''
  });
  const [bulkUpdatePercentage, setBulkUpdatePercentage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await fetch('/api/employees');
      if (!response.ok) throw new Error('Failed to fetch employees');
      const data = await response.json();
      setEmployees(data);
    } catch (err) {
      setError('Error fetching employees: ' + err.message);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`/api/employees/search?firstName=${searchTerm}&lastName=${searchTerm}`);
      if (!response.ok) throw new Error('Search failed');
      const data = await response.json();
      setEmployees(data);
    } catch (err) {
      setError('Search error: ' + err.message);
    }
  };

  const handleEmployeeSelect = (employee) => {
    setSelectedEmployee(employee);
    setUpdateForm({
      firstName: employee.firstName || '',
      lastName: employee.lastName || '',
      email: employee.email || '',
      salary: employee.salary || '',
      ssn: employee.ssn || '',
      dateOfBirth: employee.dateOfBirth || ''
    });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    if (!selectedEmployee) return;

    try {
      const response = await fetch(`/api/employees/${selectedEmployee.empid}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updateForm)
      });

      if (!response.ok) throw new Error('Update failed');
      await fetchEmployees();
      setSelectedEmployee(null);
      setUpdateForm({
        firstName: '',
        lastName: '',
        email: '',
        salary: '',
        ssn: '',
        dateOfBirth: ''
      });
    } catch (err) {
      setError('Update error: ' + err.message);
    }
  };

  const handleBulkUpdate = async (e) => {
    e.preventDefault();
    if (!bulkUpdatePercentage) return;

    try {
      const response = await fetch('/api/employees/bulk-update', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ percentage: parseFloat(bulkUpdatePercentage) })
      });

      if (!response.ok) throw new Error('Bulk update failed');
      await fetchEmployees();
      setBulkUpdatePercentage('');
    } catch (err) {
      setError('Bulk update error: ' + err.message);
    }
  };

  const handleLogout = () => navigate('/login');
  const handleFAQ = () => navigate('/faq');

  return (
    <div style={{
      color: '#4ee0c2',
      minHeight: '100vh',
      padding: '40px',
      fontFamily: 'Arial, sans-serif',
      backgroundColor: '#000',
      position: 'relative',
    }}>
      <div style={{ position: 'absolute', top: '20px', right: '20px', display: 'flex', gap: '15px' }}>
        <button onClick={handleFAQ} style={buttonStyle}>FAQ</button>
        <button onClick={handleLogout} style={buttonStyle}>Logout</button>
      </div>

      <h1 style={{ textAlign: 'center', marginBottom: '40px' }}>Admin Dashboard</h1>

      {error && <div style={{ color: 'red', marginBottom: '20px' }}>{error}</div>}

      <section style={cardStyle}>
        <h2>Search Employees</h2>
        <form onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="Search by Name"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={inputStyle}
          />
          <button type="submit" style={buttonStyle}>Search</button>
        </form>
      </section>

      <section style={cardStyle}>
        <h2>Employees</h2>
        <table style={{ width: '100%', color: '#fff', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>ID</th><th>Name</th><th>Email</th><th>Salary</th><th>Action</th>
            </tr>
          </thead>
          <tbody>
            {employees.map(emp => (
              <tr key={emp.empid} style={{ borderBottom: '1px solid #4ee0c2' }}>
                <td>{emp.empid}</td>
                <td>{emp.firstName} {emp.lastName}</td>
                <td>{emp.email}</td>
                <td>${emp.salary}</td>
                <td><button onClick={() => handleEmployeeSelect(emp)} style={buttonStyle}>Edit</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>

      {selectedEmployee && (
        <section style={cardStyle}>
          <h2>Update Employee</h2>
          <form onSubmit={handleUpdate}>
            {['firstName', 'lastName', 'email', 'salary', 'ssn', 'dateOfBirth'].map(field => (
              <input
                key={field}
                type={field === 'salary' ? 'number' : field === 'dateOfBirth' ? 'date' : 'text'}
                placeholder={field}
                value={updateForm[field]}
                onChange={(e) => setUpdateForm({ ...updateForm, [field]: e.target.value })}
                style={inputStyle}
              />
            ))}
            <div style={{ display: 'flex', gap: '10px' }}>
              <button type="submit" style={buttonStyle}>Update</button>
              <button type="button" style={buttonStyle} onClick={() => setSelectedEmployee(null)}>Cancel</button>
            </div>
          </form>
        </section>
      )}

      <section style={cardStyle}>
        <h2>Bulk Salary Update</h2>
        <form onSubmit={handleBulkUpdate}>
          <input
            type="number"
            placeholder="Percentage Increase (e.g. 5)"
            value={bulkUpdatePercentage}
            onChange={(e) => setBulkUpdatePercentage(e.target.value)}
            style={inputStyle}
          />
          <button type="submit" style={buttonStyle}>Apply Bulk Update</button>
        </form>
      </section>
    </div>
  );
}

// Shared Styles
const buttonStyle = {
  padding: '10px 20px',
  backgroundColor: '#4ee0c2',
  color: '#000',
  fontWeight: 'bold',
  border: 'none',
  borderRadius: '8px',
  cursor: 'pointer',
  boxShadow: '0 0 15px #4ee0c2, 0 0 30px #4ee0c2',
  transition: '0.3s',
};

const inputStyle = {
  padding: '12px',
  width: '100%',
  marginBottom: '15px',
  border: '2px solid #4ee0c2',
  backgroundColor: 'transparent',
  color: '#4ee0c2',
  borderRadius: '10px',
  outline: 'none',
  boxShadow: '0 0 10px #4ee0c2',
};

const cardStyle = {
  border: '2px solid #4ee0c2',
  boxShadow: '0 0 20px #4ee0c2',
  borderRadius: '10px',
  padding: '30px',
  backgroundColor: '#111',
  marginBottom: '40px',
};

export default AdminDashboardPage;
