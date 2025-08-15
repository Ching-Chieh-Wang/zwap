-- KEYS[1] = key to unlock (e.g., product:{id})
-- ARGV[1] = expected lock value (e.g., "__LOCKED__:<token>")
-- Returns: 1 if deleted, 0 if not deleted

local cur = redis.call('GET', KEYS[1])
if cur == ARGV[1] then
  return redis.call('DEL', KEYS[1])
else
  return 0
end
