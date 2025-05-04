import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function EmployeeDashboard() {
  const [employeeData, setEmployeeData] = useState(null);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchEmployeeData();
  }, []);

  const fetchEmployeeData = async () => {
    try {
      const response = await fetch('/api/employees/me');
      if (!response.ok) throw new Error('Failed to fetch employee data');
      const data = await response.json();
      setEmployeeData(data);
    } catch (err) {
      setError('Error fetching employee data: ' + err.message);
    }
  };

  const handleDownloadPaystub = async () => {
    try {
      const response = await fetch('/api/payroll/me/paystub', {
        method: 'GET',
      });
      if (!response.ok) throw new Error('Failed to download paystub');
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'paystub.csv';
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      alert('Error downloading paystub: ' + err.message);
    }
  };

  const handleLogout = () => {
    navigate('/login');
  };

  const handleFAQ = () => {
    navigate('/faq');
  };

  return (
    <div style={{
      color: '#4ee0c2',
      minHeight: '100vh',
      padding: '40px',
      fontFamily: 'Arial, sans-serif',
      backgroundColor: '#000',
      position: 'relative',
    }}>
      {/* Buttons Container */}
      <div style={{
        position: 'absolute',
        top: '20px',
        right: '20px',
        display: 'flex',
        gap: '15px',
      }}>
        <button
          onClick={handleFAQ}
          style={{
            padding: '10px 20px',
            backgroundColor: '#4ee0c2',
            color: '#000',
            fontWeight: 'bold',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            boxShadow: '0 0 15px #4ee0c2, 0 0 30px #4ee0c2',
            transition: '0.3s',
          }}
        >
          FAQ
        </button>
        <button
          onClick={handleLogout}
          style={{
            padding: '10px 20px',
            backgroundColor: '#4ee0c2',
            color: '#000',
            fontWeight: 'bold',
            border: 'none',
            borderRadius: '8px',
            cursor: 'pointer',
            boxShadow: '0 0 15px #4ee0c2, 0 0 30px #4ee0c2',
            transition: '0.3s',
          }}
        >
          Logout
        </button>
      </div>

      <h1 style={{ textAlign: 'center', marginBottom: '30px' }}>Employee Dashboard</h1>

      {error && <div style={{ color: 'red', marginBottom: '20px', textAlign: 'center' }}>{error}</div>}

      <div style={{
        display: 'flex',
        flexWrap: 'wrap',
        gap: '30px',
        justifyContent: 'center'
      }}>
        {/* Personal Info Card */}
        <div style={{
          border: '2px solid #4ee0c2',
          boxShadow: '0 0 20px #4ee0c2',
          borderRadius: '10px',
          padding: '20px',
          width: '300px',
          backgroundColor: '#111',
        }}>
          <h2>Personal Info</h2>
          {employeeData ? (
            <>
              <p><strong>Employee ID:</strong> {employeeData.empid}</p>
              <p><strong>Name:</strong> {employeeData.firstName} {employeeData.lastName}</p>
              <p><strong>Email:</strong> {employeeData.email}</p>
              <p><strong>Job Title:</strong> {employeeData.jobTitleName || 'N/A'}</p>
              <p><strong>Division:</strong> {employeeData.divisionName || 'N/A'}</p>
            </>
          ) : (
            <p>Loading...</p>
          )}
        </div>

        {/* Payroll Info Card */}
        <div style={{
          border: '2px solid #4ee0c2',
          boxShadow: '0 0 20px #4ee0c2',
          borderRadius: '10px',
          padding: '20px',
          width: '300px',
          backgroundColor: '#111',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center'
        }}>
          <h2>Payroll Info</h2>
          {employeeData ? (
            <>
              <p><strong>Salary:</strong> ${employeeData.salary}</p>
              <button
                onClick={handleDownloadPaystub}
                style={{
                  marginTop: '15px',
                  padding: '10px 20px',
                  backgroundColor: '#4ee0c2',
                  color: '#000',
                  fontWeight: 'bold',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  boxShadow: '0 0 15px #4ee0c2, 0 0 30px #4ee0c2',
                  transition: '0.3s',
                }}
              >
                Download Paystub CSV
              </button>
            </>
          ) : (
            <p>Loading...</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default EmployeeDashboard;