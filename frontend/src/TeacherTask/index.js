import React, { useEffect, useRef, useState } from "react";
import { useLocalStorage } from "../Util/useLocalStorage";
import jwt_decode from "jwt-decode";
import {
  Form,
  Col,
  Container,
  Row,
  Button,
  DropdownButton,
  ButtonGroup,
  Dropdown,
  ListGroup,
} from "react-bootstrap";
import doFetch from "../Util/doFetch";
import StatusBadge from "../Util/StatusBadge";

const TeacherTask = () => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");

  const [task, setTask] = useState({});
  const taskId = window.location.href.split("/tasks/")[1];

  const [assigner, setAssigner] = useState({});

  const [users, setUsers] = useState([]);
  const [username, setUsername] = useState("");
  const [students, setStudents] = useState([]);

  const [statuses, setStatuses] = useState([]);

  const [updateAssigners, setUpdateAssigners] = useState(false);

  function getUserId() {
    return jwt_decode(jwt).id;
  }

  function updateTask(prop, value) {
    const newTask = { ...task };
    newTask[prop] = value;
    setTask(newTask);
  }

  function save() {
    doFetch(`/api/tasks`, "put", jwt, task).then((taskResponse) => {
      setTask(taskResponse);
    });
  }

  function alreadyAssigned(username) {
    for (let i = 0; i < users.length; i++) {
      if (users[i].username === username) {
        return true;
      }
    }
    return false;
  }

  function getStatus(username) {
    for (let i = 0; i < statuses.length; i++) {
      if (statuses[i].username === username) {
        return statuses[i].taskStatus;
      }
    }
    return "";
  }

  useEffect(() => {
    doFetch(`/api/tasks/statuses/${taskId}`, "get", jwt).then(
      (statusesResponse) => {
        setStatuses(statusesResponse);
      }
    );
  }, [updateAssigners]);

  useEffect(() => {
    doFetch(`/api/tasks/${taskId}`, "get", jwt).then((taskResponse) => {
      setTask(taskResponse[0]);
    });
  }, []);

  useEffect(() => {
    if (username !== "") {
      const AssignTaskToUserDTO = {
        taskId: taskId,
        username: username,
      };

      doFetch(`/api/tasks/assign`, "post", jwt, AssignTaskToUserDTO).then(
        (response) => {}
      );
    }
  }, [username]);

  const prevTaskValue = useRef(task);

  useEffect(() => {
    prevTaskValue.current = task;
  }, [task]);

  useEffect(() => {
    doFetch(`/api/users/${getUserId()}`, "get", jwt).then((userResponse) => {
        setAssigner(userResponse);
    });
  }, []);

  useEffect(() => {
    doFetch("/api/users/by-role/ROLE_STUDENT", "get", jwt).then(
      (usersResponse) => {
        setStudents(usersResponse);
      }
    );
  }, []);

  useEffect(() => {
    doFetch(`/api/users/${taskId}/assigned-to`, "get", jwt).then(
      (usersResponse) => {
        setUsers(usersResponse);
        setUpdateAssigners(false);
      }
    );
  }, [updateAssigners]);

  return (
    <Container className="mt-5">
      <Row className="d-flex align-items-center">
        <Col>{<h2>{task.title}</h2>}</Col>
      </Row>

      {task ? (
        <>
          <Form.Group as={Row} className="my-3" controlId="title">
            <Form.Label column sm="3" md="2">
              Title:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <Form.Control
                onChange={(event) => updateTask("title", event.target.value)}
                type="text"
                value={task.title}
                placeholder="Task title"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="my-3" controlId="description">
            <Form.Label column sm="3" md="2">
              Description:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <Form.Control
                onChange={(event) =>
                  updateTask("description", event.target.value)
                }
                type="text"
                value={task.description}
                placeholder="Task description"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="my-3" controlId="expirationDate">
            <Form.Label column sm="3" md="2">
              Expiration Date:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <Form.Control
                onChange={(event) =>
                  updateTask("expirationDate", event.target.value)
                }
                type="text"
                value={task.expirationDate}
                placeholder="Task expiration Date"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="my-3" controlId="assignedByUserId">
            <Form.Label column sm="3" md="2">
              Assigned By:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <Form.Control
                type="text"
                readOnly
                value={assigner.name}
                placeholder="Assigned By"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="my-3" controlId="assign">
            <Form.Label column sm="3" md="2">
              Assign To:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <DropdownButton
                as={ButtonGroup}
                variant={"btn btn-outline-secondary"}
                title={"Assign To"}
                onSelect={(selectedElement) => {
                  setUpdateAssigners(true);
                  !alreadyAssigned(selectedElement)
                    ? setUsername(selectedElement)
                    : alert("Task has already assigned to this student");
                }}
              >
                {students.map((student) => (
                  <Dropdown.Item eventKey={student.username}>
                    {student.name}
                  </Dropdown.Item>
                ))}
              </DropdownButton>
            </Col>
          </Form.Group>

          {users.map((user) => (
            <ListGroup as="ol" numbered key={user.id}>
              <Col sm="9" md="8" lg="6">
                <ListGroup.Item
                  as="li"
                  className="d-flex justify-content-between align-items-start rounded my-2"
                >
                  <div className="d-flex justify-content-between align-items-center w-100">
                    <div className="ms-2 me-auto">
                      <div className="fw-bold">{user.name}</div>
                      Assign to
                    </div>
                    <div className="ms-2">
                      {getStatus(user.username) === "IN_REVIEW" && (
                        <Button
                          variant={"btn btn-outline-secondary"}
                          onClick={() => {
                            window.location.href = `/tasks/results/${taskId}/${user.id}`;
                          }}
                        >
                          Show results
                        </Button>
                      )}
                    </div>
                    <div className="ms-2">
                      <StatusBadge text={getStatus(user.username)} />
                    </div>
                  </div>
                </ListGroup.Item>
              </Col>
            </ListGroup>
          ))}

          <div className="d-flex gap-4 my-3">
            <Button onClick={() => save()} variant="btn btn-outline-secondary">
              Submit
            </Button>

            <Button
              onClick={() => {
                window.location.href = `tests/${taskId}`;
              }}
              variant="btn btn-outline-secondary"
            >
              Add tests
            </Button>

            <Button
              onClick={() => {
                window.location.href = "/dashboard";
              }}
              variant="btn btn-outline-secondary"
            >
              Back to tasks page
            </Button>
          </div>
        </>
      ) : (
        <> </>
      )}
    </Container>
  );
};

export default TeacherTask;
