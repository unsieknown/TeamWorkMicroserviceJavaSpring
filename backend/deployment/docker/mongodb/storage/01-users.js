db = db.getSiblingDB("team-work-storage")
db.createUser({
    user: "team-work-storage-admin",
    roles: [
        {role: "dbAdmin", db: "team-work-storage"},
        {role: "dbOwner", db: "team-work-storage"},
        {role: "userAdmin", db: "team-work-storage"}
    ],
    pwd: "superSecretPassword"
})