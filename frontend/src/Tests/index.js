import React, { useEffect, useRef, useState } from "react";
import { Container, Row, Col, Form, Button } from "react-bootstrap";
import doFetch from "../Util/doFetch";
import { useLocalStorage } from "../Util/useLocalStorage";

const Tests = () => {
  const [jwt, setJwt] = useLocalStorage("", "jwt");
  const [tests, setTests] = useState([]);
  const [rows, setRows] = useState(0);

  const taskId = window.location.href.split("/tests/")[1];

  const [taskProps, setTaskProps] = useState({
    codeTemplate: null,
    taskId: taskId,
  });
  const [alreadyExists, SetAlreadyExists] = useState(true);

  useEffect(() => {
    doFetch(`/api/tests/${taskId}`, "get", jwt).then((testsResponse) => {
      setTests(testsResponse);
      setRows(testsResponse.length);
    });
  }, []);

  const handleAddRow = () => {
    setRows(rows + 1);
  };

  function sendTests() {
    var testsRequest = Object.values(tests);

    doFetch(`/api/tests/${taskId}`, "post", jwt, testsRequest).then(
      (response) => {}
    );
  }

  const prevTests = useRef(tests);

  useEffect(() => {
    prevTests.current = tests;
  }, [tests]);

  function updateTests(form, index, value, isNew) {
    const newTests = { ...tests };
    if (isNew) {
      const newElement = {
        input: "",
        output: "",
      };
      newTests[index] = newElement;
    }
    if (form === "input") newTests[index].input = value;
    if (form === "output") newTests[index].output = value;
    setTests(newTests);
  }

  useEffect(() => {
    doFetch(`/api/task-props/${taskId}`, "get", jwt).then(
      (taskPropsResponse) => {
        if (taskPropsResponse.codeTemplate === null) {
          SetAlreadyExists(false);
        }
        setTaskProps(taskPropsResponse);
      }
    );
  }, []);

  function putTaskProps() {
    doFetch("/api/task-props", "put", jwt, taskProps).then((response) => {});
  }

  function postTaskProps() {
    doFetch("/api/task-props", "post", jwt, taskProps).then((response) => {});
  }

  function updateCodeTemplate(value) {
    const taskPropsCopy = { ...taskProps };
    taskPropsCopy.codeTemplate = value;
    setTaskProps(taskPropsCopy);
  }

  return (
    <Container className="mt-3 mb-5">
      <Row className="mb-3">
        <Col className="text-center">
          <h3>Creating function template</h3>
        </Col>
      </Row>
      <div className="mt-4">
        <Col className="col-md-6 offset-md-3">
          <textarea
            style={{ width: "100%", borderRadius: "0.5em", height: "200px" }}
            onChange={(event) => updateCodeTemplate(event.target.value)}
            value={taskProps.codeTemplate}
          ></textarea>

          <Button
            onClick={() => {
              if (alreadyExists) {
                putTaskProps();
              } else {
                postTaskProps();
              }
            }}
            variant="btn btn-outline-secondary"
          >
            Create
          </Button>
        </Col>
      </div>

      <Row className="mb-3 mt-5">
        <Col className="text-center">
          <h3>Adding tests to task</h3>
        </Col>
      </Row>

      {Array.from({ length: rows }).map((_, index) => (
        <Row key={index} className="mt-3">
          <Col>
            <Form>
              <Form.Group as={Row}>
                <Col>
                  <Form.Control
                    type="text"
                    placeholder="Input"
                    value={tests[index]?.input || ""}
                    onChange={(event) => {
                      if (!tests[index]) {
                        updateTests("input", index, event.target.value, true);
                      } else {
                        updateTests("input", index, event.target.value, false);
                      }
                    }}
                  />
                </Col>
                <Col>
                  <Form.Control
                    type="text"
                    placeholder="Output"
                    value={tests[index]?.output || ""}
                    onChange={(event) => {
                      if (!tests[index]) {
                        updateTests("output", index, event.target.value, true);
                      } else {
                        updateTests("output", index, event.target.value, false);
                      }
                    }}
                  />
                </Col>
              </Form.Group>
            </Form>
          </Col>
        </Row>
      ))}

      <Row className="mt-3">
        <Col>
          <Button onClick={handleAddRow} variant={"btn btn-outline-secondary"}>
            Add Row
          </Button>
        </Col>
      </Row>

      <Row className="mt-3">
        <Col>
          <Button
            onClick={() => {
              sendTests();
            }}
            variant={"btn btn-outline-secondary"}
          >
            Add tests
          </Button>
        </Col>
      </Row>

      <Row className="mt-5">
        <Col>
          <Button
            onClick={() => {
              window.location.href = `/tasks/${taskId}`;
            }}
            variant={"btn btn-outline-secondary"}
          >
            Back to task page
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default Tests;
