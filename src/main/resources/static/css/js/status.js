// status.js
function updateStatus(selectEl, projectId, taskId) {
    const status = selectEl.value;

    fetch(`/projects/${projectId}/tasks/${taskId}/update-status-ajax`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
        },
        body: new URLSearchParams({ status }),
    })
        .then((res) => res.text())
        .then((text) => {
            if (text !== "OK") {
                alert("Status kunne ikke gemmes. Prøv igen.");
            }
        })
        .catch(() => {
            alert("Netværksfejl – status blev ikke gemt.");
        });
}
