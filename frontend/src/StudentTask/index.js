import React, { useEffect, useRef, useState } from "react";
import { useLocalStorage } from "../Util/useLocalStorage";
import { Form, Button, Col, Container, Row, Table } from "react-bootstrap";
import StatusBadge from "../Util/StatusBadge";
import doFetch from "../Util/doFetch";
import jwt_decode from "jwt-decode";
import { FaCheck, FaTimes } from "react-icons/fa";

const StudentTask = () => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");

  const [codeTemplateExists, SetCodeTemplateExists] = useState(false);
  const [taskProps, SetTaskProps] = useState({});

  const [needsPosting, setNeedsPosting] = useState(false);

  function getUserId() {
    return jwt_decode(jwt).id;
  }

  const taskId = window.location.href.split("/tasks/")[1];

  const [task, setTask] = useState(
    {
      id: null,
      title: null,
      description: null,
      status: null,
      expirationDate: null,
      assignedByUserId: null,
    },
    []
  );

  const [assigner, setAssigner] = useState({
    id: null,
    name: null,
    username: null,
  });

  useEffect(() => {
    doFetch(`/api/tasks/${taskId}/assigned-to/${getUserId()}`, "get", jwt).then(
      (taskResponse) => {
        setTask(taskResponse);
      }
    );
  }, []);

  useEffect(() => {
    if (task.assignedByUserId) {
      doFetch(`/api/users/${task.assignedByUserId}`, "get", jwt).then(
        (assignerResponse) => {
          setAssigner(assignerResponse);
        }
      );
    }
  }, [task]);

  const [solution, setSolution] = useState({
    text: null,
    postingTime: null,
    userId: getUserId(),
    taskId: taskId,
  });

  useEffect(() => {
    doFetch(`/api/task-props/${taskId}`, "get", jwt).then(
      (taskPropsResponse) => {
        if (taskPropsResponse.codeTemplate !== null) {
          SetCodeTemplateExists(true);
        }
        SetTaskProps(taskPropsResponse);
        setSolution((prevSolution) => ({
          ...prevSolution,
          text: taskPropsResponse.codeTemplate,
        }));
      }
    );
  }, []);

  useEffect(() => {
    doFetch(`/api/solutions/${getUserId()}/${taskId}`, "get", jwt).then(
      (solutionResponse) => {
        if (!solutionResponse.message) {
          setSolution(solutionResponse);
        } else {
          setNeedsPosting(true);
          updateSolution(taskProps.codeTemplate);
        }
      }
    );
  }, []);

  const prevSolution = useRef(solution);

  useEffect(() => {
    prevSolution.current = solution;
  }, [solution]);

  function updateSolution(value) {
    const solutionCopy = { ...solution };
    solutionCopy.text = value;
    solutionCopy.userId = getUserId();
    solutionCopy.taskId = taskId;
    setSolution(solutionCopy);
  }

  function postSolution() {
    doFetch("/api/solutions", "post", jwt, solution).then((response) => {
      setNeedsPosting(false);
    });
  }

  function putSolution() {
    doFetch("/api/solutions", "put", jwt, solution).then((response) => {});
  }

  const [listOfResults, setListOfResults] = useState([]);

  const [comments, setComments] = useState([]);

  const [error, setError] = useState({
  });

  useEffect(() => {
    doFetch(`/api/comments/${taskId}/${getUserId()}`, "get", jwt).then(
      (response) => {
        setComments(response);
      }
    );
  }, []);

  function saveResults() {
    doFetch(
      `/api/verification/${getUserId()}/${taskId}`,
      "post",
      jwt,
      listOfResults
    ).then((response) => {
      setListOfResults(response);
    });
  }

  function updateStatus(status) {
    const statusDTO = {
      value: status,
    };
    doFetch(
      `/api/tasks/${taskId}/${getUserId()}/update-status`,
      "put",
      jwt,
      statusDTO
    ).then((response) => {});
  }

  return (
    <Container className="mt-5">
      <Row className="d-flex align-items-center">
        <Col>{<h2>{task.title}</h2>}</Col>
        <Col>
          <StatusBadge text={task.status} />
        </Col>
      </Row>

      {task ? (
        <>
          <Form.Group as={Row} className="my-3" controlId="title">
            <Form.Label column sm="3" md="2">
              Title:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <Form.Control
                onChange={(event) => console.log("")}
                type="text"
                readOnly
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
                onChange={(event) => console.log("")}
                type="text"
                readOnly
                value={task.description}
                placeholder="Task description"
              />
            </Col>
          </Form.Group>

          <Form.Group as={Row} className="my-3" controlId="expirationDate">
            <Form.Label column sm="3" md="2">
              Expiration time:
            </Form.Label>
            <Col sm="9" md="8" lg="6">
              <Form.Control
                onChange={(event) => console.log("")}
                type="text"
                readOnly
                value={new Date(task.expirationDate).toLocaleString("en-GB", {
                  month: "long",
                  day: "numeric",
                  hour: "numeric",
                  minute: "numeric",
                })}
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
                onChange={(event) => console.log("")}
                type="text"
                readOnly
                value={assigner.name}
                placeholder="Assigned By"
              />
            </Col>
          </Form.Group>
        </>
      ) : (
        <> </>
      )}

      {codeTemplateExists ? (
        <div className="mt-4">
          <Col sm="9" md="8" lg="9">
            <textarea
              style={{ width: "100%", borderRadius: "0.5em", height: "200px" }}
              onChange={(event) => {
                updateSolution(event.target.value);
              }}
              value={solution.text}
            ></textarea>
          </Col>
          {error.value ? (
            <h3 style={{color:"red"}}>Error: {error.value}</h3>
          ) : (
            <></>
          )}
          <Button
            onClick={() => {
              console.log("list of results: ", listOfResults);
              if (needsPosting) {
                postSolution();
              } else {
                putSolution();
              }
              saveResults();
              updateStatus("IN_REVIEW");
            }}
            variant="btn btn-outline-secondary"
          >
            Post task solution
          </Button>
          <Col className="mt-2">
            <Button
              onClick={() => {
                const request = {
                  solutionText: solution.text,
                  taskId: taskId,
                };
                doFetch("/api/verification", "post", jwt, request).then(
                  (response) => {
                    console.log("verification response: ", response);
                    if ("value" in response) {
                      setError(response);
                    } else {
                      setError({value: null});
                      setListOfResults(response);
                    }
                  }
                );
              }}
              variant="btn btn-outline-secondary"
            >
              Run
            </Button>
          </Col>
          {listOfResults[0] ? (
            <div className="mt-4">
              <h2>Results</h2>
              <Table striped bordered>
                <thead>
                  <tr>
                    <th>Received</th>
                    <th>Expected</th>
                    <th>Result</th>
                  </tr>
                </thead>
                <tbody>
                  {listOfResults.map((result, index) => (
                    <tr key={index}>
                      <td>{result.received}</td>
                      <td>{result.expected}</td>
                      <td>
                        {result.result ? (
                          <FaCheck className="text-success" />
                        ) : (
                          <FaTimes className="text-danger" />
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          ) : (
            <></>
          )}
          {/* {error ? (
            <h>{error.value}</h>
          ) : (
            <></>
          )} */}
        </div>
      ) : (
        <></>
      )}

      <div className="mt-4">
        <Col lg="6">
          <h4>Comments:</h4>
          {comments.map((comment) => (
            <div key={comment.id} className="card mt-2">
              <div className="card-body">
                <p className="card-text">{comment.text}</p>
                <p className="card-subtitle mb-2 text-muted">
                  Posted by {assigner.name}:{" "}
                  {new Date(comment.createdDate).toLocaleString("en-GB", {
                    month: "long",
                    day: "numeric",
                    hour: "numeric",
                    minute: "numeric",
                  })}
                </p>
              </div>
            </div>
          ))}
        </Col>
      </div>

      <div className="mt-4 mb-4">
        <Button
          onClick={() => {
            window.location.href = "/dashboard";
          }}
          variant="btn btn-outline-secondary"
        >
          Back to tasks page
        </Button>
      </div>
    </Container>
  );
};

export default StudentTask;
