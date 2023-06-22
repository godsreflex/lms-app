import React, { useState } from "react";
import { useLocalStorage } from "../Util/useLocalStorage";
import { Button, Col, Container, Row, Form, Alert } from "react-bootstrap";
import doFetch from "../Util/doFetch";

const Login = () => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [refresh, setRefresh] = useLocalStorage("", "refresh");

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState({
    message: "",
    errors: null,
  });

  function sendAuthenticationRequest() {
    const authenticationRequestBody = {
      username: username,
      password: password,
    };

    doFetch("api/auth/login", "post", null, authenticationRequestBody).then(
      (response) => {
        if ("message" in response) setError(response);
        else {
          setJwt(response.accessToken);
          setRefresh(response.refreshToken);
          window.location.href = "dashboard";
        }
      }
    );
  }

  function getError(error, field) {
    if (field) return error.errors[field];
    return error.message;
  }

  return (
    <>
      <Container>
        <Row className="justify-content-center">
          <Col md="6">
            <Form.Group className="my-5" controlId="username">
              <Form.Label className="fs-5">Username</Form.Label>
              <div className="text-danger">
                {error.errors ? getError(error, "username") : " "}
              </div>
              <Form.Control
                type="email"
                value={username}
                placeholder="Enter your email"
                onChange={(event) => setUsername(event.target.value)}
              ></Form.Control>
            </Form.Group>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="6">
            <Form.Group className="mb-3" controlId="password">
              <Form.Label className="fs-5">Password</Form.Label>
              <div className="text-danger">
                {error.errors ? getError(error, "password") : " "}
              </div>
              <Form.Control
                type="password"
                value={password}
                placeholder="Enter your password"
                onChange={(event) => setPassword(event.target.value)}
              ></Form.Control>
            </Form.Group>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col
            className="mt-2 d-flex flex-column gap-2 flex-md-row justify-content-center"
            md="6"
          >
            <Button
              id="submit"
              type="button"
              onClick={() => sendAuthenticationRequest()}
              variant="btn btn-outline-secondary"
            >
              Login
            </Button>
          </Col>
        </Row>
        <div
          className="mt-2 d-flex flex-column gap-2 flex-md-row justify-content-center text-danger"
          md="6"
        >
          {getError(error)}
        </div>
      </Container>
    </>
  );
};

export default Login;
