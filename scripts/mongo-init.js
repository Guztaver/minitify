// Initialize MongoDB database
db = db.getSiblingDB('minitify');

// Create the minitify user for the application
db.createUser({
  user: 'minitify',
  pwd: 'minitify123',
  roles: [
    {
      role: 'readWrite',
      db: 'minitify'
    }
  ]
});

// Create initial collections (optional)
db.createCollection('artists');
db.createCollection('albums');
db.createCollection('musics');
db.createCollection('playlists');

print('MongoDB initialized successfully for minitify application');