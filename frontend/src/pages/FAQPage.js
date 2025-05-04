import React from 'react';
import { useNavigate } from 'react-router-dom';

function FAQPage() {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate(-1); // Go back to the previous page
  };

  const faqItems = [
    {
      question: "How do I update my personal information?",
      answer: "You can update your personal information by contacting your HR representative or through the employee portal if you have the necessary permissions."
    },
    {
      question: "How do I view my pay history?",
      answer: "Your pay history can be viewed in the Payroll Info section of your dashboard. If you need historical data beyond what's shown, please contact HR."
    },
    {
      question: "What should I do if I notice an error in my information?",
      answer: "If you notice any errors in your personal or payroll information, please contact your HR representative immediately to have it corrected."
    },
    {
      question: "How do I change my password?",
      answer: "To change your password, log out and use the 'Forgot Password' option on the login page. You will receive instructions via email."
    },
    {
      question: "Who do I contact for technical support?",
      answer: "For technical support, please contact the IT helpdesk at helpdesk@company.com or call extension 5555."
    },
    {
      question: "How do I access my tax documents?",
      answer: "Tax documents are available in the Payroll Info section of your dashboard. You can download them as PDF files."
    },
    {
      question: "What is the process for requesting time off?",
      answer: "Time off requests should be submitted through the HR portal at least two weeks in advance. Your manager will review and approve the request."
    },
    {
      question: "How do I update my direct deposit information?",
      answer: "Direct deposit information can be updated in the Payroll Info section. Changes must be submitted by the 15th of the month to take effect for the next pay period."
    }
  ];

  return (
    <div style={{
      color: '#4ee0c2',
      minHeight: '100vh',
      padding: '40px',
      fontFamily: 'Arial, sans-serif',
      position: 'relative',
    }}>
      {/* Back Button */}
      <button
        onClick={handleBack}
        style={{
          position: 'absolute',
          top: '20px',
          left: '20px',
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
        Back
      </button>

      <h1 style={{ textAlign: 'center', marginBottom: '40px' }}>Frequently Asked Questions</h1>

      <div style={{
        maxWidth: '800px',
        margin: '0 auto',
      }}>
        {faqItems.map((item, index) => (
          <div
            key={index}
            style={{
              border: '2px solid #4ee0c2',
              boxShadow: '0 0 20px #4ee0c2',
              borderRadius: '10px',
              padding: '20px',
              marginBottom: '20px',
              backgroundColor: '#111',
            }}
          >
            <h2 style={{ color: '#4ee0c2', marginBottom: '10px' }}>{item.question}</h2>
            <p style={{ color: '#fff' }}>{item.answer}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default FAQPage;