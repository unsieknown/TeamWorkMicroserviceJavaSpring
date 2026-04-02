db = db.getSiblingDB("team-work-board")
db.auth("team-work-board-admin", "superSecretPassword")

db.boards.createIndex({
    "members.userId": 1,
    teamId: 1
})

db.boards.createIndex({
    "owner.userId": 1,
    deleted: 1,
    archived: 1
})

db.tasks.createIndex(
    {
        title: 1
    }
)