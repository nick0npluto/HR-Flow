// src/components/Header.js
import React, { useEffect, useState } from "react";

export default function Header() {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetch("/api/auth/user")
      .then(res => res.json())
      .then(data => {
        setUser(data);
        setIsLoading(false);
      })
      .catch(() => {
        setIsLoading(false);
      });
  }, []);

  return (
    <header>
      {isLoading ? "Loading..." : (user ? `Welcome, ${user.username}` : "Please log in")}
    </header>
  );
}