db = db.getSiblingDB("team-work-notes")
db.auth("team-work-notes-admin", "password")

db.notes.createIndex(
    {
        "title": "text",
        "content": "text"
    }
)