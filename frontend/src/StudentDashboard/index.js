import React, { useEffect, useState } from "react";
import { useLocalStorage } from "../Util/useLocalStorage";
import jwt_decode from "jwt-decode";
import doFetch from "../Util/doFetch";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import StatusBadge from "../Util/StatusBadge";

const StudentDashboard = () => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [tasks, setTasks] = useState(null);

  const [assignerNames, setAssignerNames] = useState({});

  function getUserId() {
    return jwt_decode(jwt).id;
  }

  function formatExpirationDate(dateString) {
    const date = new Date(dateString);
    const options = { year: "numeric", month: "long", day: "numeric" };
    return date.toLocaleDateString("en-US", options);
  }

  async function getUserNameById(id) {
    const userResponse = await doFetch(`api/users/${id}`, "get", jwt);
    return userResponse.name;
  }

  async function loadAssignerName(id) {
    const name = await getUserNameById(id);
    setAssignerNames((prevState) => ({
      ...prevState,
      [id]: name,
    }));
  }

  useEffect(() => {
    doFetch(`api/tasks/by-user-id/${getUserId()}`, "get", jwt).then(
      (tasksResponse) => {
        setTasks(tasksResponse);
        tasksResponse.forEach((task) => {
          loadAssignerName(task.assignedByUserId);
        });
      }
    );
  }, []);

  return (
    <Container>
      <Row>
        <Col>
          <div
            className="d-flex justify-content-end"
            style={{ cursor: "pointer" }}
            onClick={() => {
              setJwt(null);
              window.location.href = "/login";
            }}
          >
            Logout
          </div>
        </Col>
      </Row>

      <div className="task-wrapper">
        <div className="h5 px-3 task-wrapper-title">IN REVIEW</div>
        {tasks &&
        tasks.filter((task) => task.status === "IN_REVIEW").length > 0 ? (
          <div
            className="d-grid gap-2"
            style={{ gridTemplateColumns: "repeat(auto-fill, 18rem)" }}
          >
            {tasks
              .filter((task) => task.status === "IN_REVIEW")
              .map((task) => (
                <Card key={task.id} style={{ width: "18rem", height: "18rem" }}>
                  <Card.Body className="d-flex flex-column justify-content-around">
                    <Card.Title>{task.title}</Card.Title>
                    <div className="d-flex align-items-start">
                      <StatusBadge text={"IN_REVIEW"} />
                    </div>
                    <Card.Text>
                      <p>
                        <b>Assigned by:</b>{" "}
                        {assignerNames[task.assignedByUserId]}
                      </p>
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
                      View
                    </Button>
                  </Card.Body>
                </Card>
              ))}
          </div>
        ) : (
          <div>No tasks found</div>
        )}
      </div>

      <div className="task-wrapper">
        <div className="h5 px-3 task-wrapper-title">AWAITING</div>
        {tasks &&
        tasks.filter((task) => task.status === "AWAITING").length > 0 ? (
          <div
            className="d-grid gap-2"
            style={{ gridTemplateColumns: "repeat(auto-fill, 18rem)" }}
          >
            {tasks
              .filter((task) => task.status === "AWAITING")
              .map((task) => (
                <Card key={task.id} style={{ width: "18rem", height: "18rem" }}>
                  <Card.Body className="d-flex flex-column justify-content-around">
                    <Card.Title>{task.title}</Card.Title>
                    <div className="d-flex align-items-start">
                      <StatusBadge text={"AWAITING"} />
                    </div>
                    <Card.Text>
                      <p>
                        <b>Assigned by:</b>{" "}
                        {assignerNames[task.assignedByUserId]}
                      </p>
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
                      View
                    </Button>
                  </Card.Body>
                </Card>
              ))}
          </div>
        ) : (
          <div>No tasks found</div>
        )}
      </div>

      <div className="task-wrapper">
        <div className="h5 px-3 task-wrapper-title">REJECTED</div>
        {tasks &&
        tasks.filter((task) => task.status === "REJECTED").length > 0 ? (
          <div
            className="d-grid gap-2"
            style={{ gridTemplateColumns: "repeat(auto-fill, 18rem)" }}
          >
            {tasks
              .filter((task) => task.status === "REJECTED")
              .map((task) => (
                <Card key={task.id} style={{ width: "18rem", height: "18rem" }}>
                  <Card.Body className="d-flex flex-column justify-content-around">
                    <Card.Title>{task.title}</Card.Title>
                    <div className="d-flex align-items-start">
                      <StatusBadge text={"REJECTED"} />
                    </div>
                    <Card.Text>
                      <p>
                        <b>Assigned by:</b>{" "}
                        {assignerNames[task.assignedByUserId]}
                      </p>
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
                      View
                    </Button>
                  </Card.Body>
                </Card>
              ))}
          </div>
        ) : (
          <div>No tasks found</div>
        )}
      </div>

      <div className="task-wrapper">
        <div className="h5 px-3 task-wrapper-title">COMPLETED</div>
        {tasks &&
        tasks.filter((task) => task.status === "COMPLETED").length > 0 ? (
          <div
            className="d-grid gap-2"
            style={{ gridTemplateColumns: "repeat(auto-fill, 18rem)" }}
          >
            {tasks
              .filter((task) => task.status === "COMPLETED")
              .map((task) => (
                <Card key={task.id} style={{ width: "18rem", height: "18rem" }}>
                  <Card.Body className="d-flex flex-column justify-content-around">
                    <Card.Title>{task.title}</Card.Title>
                    <div className="d-flex align-items-start">
                      <StatusBadge text={"COMPLETED"} />
                    </div>
                    <Card.Text>
                      <p>
                        <b>Assigned by:</b>{" "}
                        {assignerNames[task.assignedByUserId]}
                      </p>
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
                      View
                    </Button>
                  </Card.Body>
                </Card>
              ))}
          </div>
        ) : (
          <div>No tasks found</div>
        )}
      </div>
    </Container>
  );
};

export default StudentDashboard;
