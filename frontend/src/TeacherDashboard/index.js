import React, { useEffect, useState } from "react";
import { useLocalStorage } from "../Util/useLocalStorage";
import jwt_decode from "jwt-decode";
import doFetch from "../Util/doFetch";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import StatusBadge from "../Util/StatusBadge";

const TeacherDashboard = () => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [tasks, setTasks] = useState(null);

  function getUserId() {
    return jwt_decode(jwt).id;
  }

  function formatExpirationDate(dateString) {
    const date = new Date(dateString);
    const options = { year: "numeric", month: "long", day: "numeric" };
    return date.toLocaleDateString("en-US", options);
  }

  function createAssignment(assignedByUserId) {
    doFetch(
      `/api/tasks/${assignedByUserId}`,
      "post",
      jwt
    ).then((taskResponse) => {
      console.log(taskResponse);
      window.location.href = `/tasks/${taskResponse.id}`;
    });
  }

  useEffect(() => {
    doFetch(`/api/tasks/by-assigner-id/${getUserId()}`, "get", jwt).then(
      (tasksResponse) => {
        setTasks(tasksResponse);
      }
    );
  }, [tasks]);

  return (
    <Container>
      <Row>
        <Col>
          <div
            className="d-flex justify-content-end"
            style={{ cursor: "pointer" }}
            onClick={() => {
              setJwt(null);
              console.log("jwt ", jwt);
              window.location.href = "/login";
            }}
          >
            Logout
          </div>
        </Col>
      </Row>

      <div className="mb-2">
        <Button
          onClick={() => createAssignment(getUserId())}
          variant="btn btn-outline-secondary"
          size="lg"
        >
          Submit a new task
        </Button>
      </div>

      {tasks ? (
        <div
          className="d-grid gap-2"
          style={{ gridTemplateColumns: "repeat(auto-fill, 18rem)" }}
        >
          {tasks.map((task) => (
            <Card key={task.id} style={{ width: "18rem", height: "18rem" }}>
              <Card.Body className="d-flex flex-column justify-content-around">
                <Card.Title>{task.title}</Card.Title>
                <Card.Text>
                  <p>
                    <b>Expiration time: </b>{" "}
                    {formatExpirationDate(task.expirationDate)}
                  </p>
                </Card.Text>
                <Button
                  onClick={() => {
                    window.location.href = `/tasks/${task.id}`;
                  }}
                  variant="btn btn-outline-secondary"
                >
                  Edit
                </Button>
              </Card.Body>
            </Card>
          ))}
        </div>
      ) : (
        <></>
      )}
    </Container>
  );
};

export default TeacherDashboard;
