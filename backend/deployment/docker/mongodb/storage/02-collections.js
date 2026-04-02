db = db.getSiblingDB("team-work-storage")
db.auth("team-work-storage-admin", "superSecretPassword")

db.runCommand({
    create: "image_metadata",
    validator: {
        $jsonSchema: {
            bsonType: "object",
            title: "Image Metadata Schema Validation",
            required: ["originalName", "storedName", "ownerId", "extension", "size", "createdAt"],
            additionalProperties: false,
            properties: {
                _id:{
                    bsonType: "objectId"
                },
                _class: {
                    bsonType: "string",
                    enum: ["image_metadata"]
                },
                originalName: {
                    bsonType: "string",
                    title: "Original File Name",
                    description: "Original File Name Is Required"
                },
                storedName: {
                    bsonType: "string",
                    title: "Stored File Name",
                    description: "Stored File Name Is Required"
                },
                ownerId: {
                    bsonType: "binData",
                    title: "Id Of The Owner",
                    description: "Owner Id Is Required"
                },
                extension: {
                    bsonType: "string",
                    title: "File Extension",
                    description: "Extension Is Required",
                    enum: ["jpg", "png"]
                },
                size: {
                    bsonType: "long",
                    title: "File Size",
                    description: "File Size Is Required",
                    minimum: 0
                },
                createdAt: {
                    bsonType: "date"
                }
            }
        }
    }
})