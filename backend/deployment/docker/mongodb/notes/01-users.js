db = db.getSiblingDB("team-work-notes")
db.createUser({
    user: "team-work-notes-admin",
    roles: [
        {role: "dbAdmin", db: "team-work-notes"},
        {role: "dbOwner", db: "team-work-notes"},
        {role: "userAdmin", db: "team-work-notes"}
    ],
    pwd: "superSecretPassword"
})