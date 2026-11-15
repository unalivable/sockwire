# SockWire Core

Mobile wrapper for `wireproxy`. The CI workflow clones **your fork** of
`pufferffish/wireproxy` (or `whyvl/wireproxy`), injects `mobile/core.go`
from this folder, and runs `gomobile bind`.

## One-time setup

1. Fork https://github.com/pufferffish/wireproxy to your account.
2. In `.github/workflows/build-core.yml`, set `CORE_REPO` to your fork URL.

That is it. `mobile/core.go` stays here, in the main repo.
