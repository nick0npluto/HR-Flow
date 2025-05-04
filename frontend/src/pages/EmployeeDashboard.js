import React, { useState, useEffect } from 'react';

function EmployeeDashboard() {
  const [employeeData, setEmployeeData] = useState(null);
  const [error, setError] = useState('');

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

  return (
    <div className="employee-dashboard">
      <h1>Employee Dashboard</h1>
      
      {error && <div className="error">{error}</div>}

      {employeeData && (
        <div className="employee-info">
          <h2>Your Information</h2>
          <div className="info-section">
            <p><strong>Name:</strong> {employeeData.firstName} {employeeData.lastName}</p>
            <p><strong>Email:</strong> {employeeData.email}</p>
            <p><strong>Salary:</strong> ${employeeData.salary}</p>
            <p><strong>Date of Birth:</strong> {employeeData.dateOfBirth}</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default EmployeeDashboard; 