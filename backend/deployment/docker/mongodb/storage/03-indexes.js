db = db.getSiblingDB("team-work-storage")
db.auth("team-work-storage-admin", "superSecretPassword")

db.image_storage.createIndex({
    ownerId: 1
})