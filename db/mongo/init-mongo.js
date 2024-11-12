db.createUser({
    user: 'root',
    pwd: 'toor',
    roles: [
        {
            role: 'readWrite',
            db: 'testDB',
        },
    ],
});
db.createCollection('app_users', { capped: false });

db.app_users.insert([
    {
        "username": "ragnar777",
        "dni": "VIKI771012HMCRG093",
        "enabled": true,
        "password": "$2a$10$VV2WjbpSnKa2RUl0xEcQw.MF2JTQ3N39Suxdgg5p3tTaTyKEOaOsm",
        "role":
            {
                "granted_authorities": ["ROLE_USER"]
            }
    },
    {
        "username": "heisenberg",
        "dni": "BBMB771012HMCRR022",
        "enabled": true,
        "password": "$2a$10$tgmS0HU2csPTKbZNi/BF/Op8CEfr46AZk96ml8lQfF0I7l1HYGxB2",
        "role":
            {
                "granted_authorities": ["ROLE_USER"]
            }
    },
    {
        "username": "misterX",
        "dni": "GOTW771012HMRGR087",
        "enabled": true,
        "password": "$2a$10$d7xyNl5sVh7I4ytZ7mlfqOxStjSl4Irt9LD2U2xdSLP6US39EOhRa",
        "role":
            {
                "granted_authorities": ["ROLE_USER", "ROLE_ADMIN"]
            }
    },
    {
        "username": "neverMore",
        "dni": "WALA771012HCRGR054",
        "enabled": true,
        "password": "$2a$10$gtnspUdSvGgbNolE1P/AhuLXWhyLo5AmzYSIpQDVQTzZRH8BGml6.",
        "role":
            {
                "granted_authorities": ["ROLE_ADMIN"]
            }
    }
]);