db = db.getSiblingDB("team-work-board")
db.createUser({
    user: "team-work-board-admin",
    roles: [
        {role: "dbAdmin", db: "team-work-board"},
        {role: "dbOwner", db: "team-work-board"},
        {role: "userAdmin", db: "team-work-board"}
    ],
    pwd: "superSecretPassword"
})