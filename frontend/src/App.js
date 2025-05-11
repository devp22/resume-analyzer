import "./App.css";
import React, { useState } from "react";
import axios from "axios";
import { FileUploader } from "react-drag-drop-files";
import TextareaAutosize from "react-textarea-autosize";
import ParticleBackground from "./ParticleBackground";

const fileTypes = ["PDF"];

function App() {
  const [file, setFile] = useState(null);
  const [text, setText] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (uploadedFile) => {
    setFile(uploadedFile);
    setText("");
  };

  const handleSubmit = async () => {
    if (!file || !description) {
      alert("Please upload a resume and enter a job description.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("description", description);
    setLoading(true);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/extract",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      setText(response.data);
    } catch (error) {
      console.error("Error uploading file:", error);
      setText("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h1>Resume Skill Analyzer</h1>

      <div className="section">
        <label>Upload Resume (PDF):</label>
        <FileUploader
          handleChange={handleChange}
          name="file"
          types={fileTypes}
          hoverTitle="Drag or Drop your Resume here"
        />
        {file && <p className="filename">Selected file: {file.name}</p>}
      </div>

      <div className="section">
        <label>Paste Job Description:</label>
        <TextareaAutosize
          className="textarea"
          minRows={4}
          placeholder="Paste or type the job description here..."
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>

      <button className="button" onClick={handleSubmit} disabled={loading}>
        {loading ? "Analyzing..." : "Analyze Resume"}
      </button>

      {text && (
        <div className="result">
          <h2>Analysis Result:</h2>
          <pre>{text}</pre>
        </div>
      )}
    </div>
  );
}

export default App;
