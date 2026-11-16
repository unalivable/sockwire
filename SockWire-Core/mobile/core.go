// Package mobile is the gomobile entrypoint for SockWire Core.
// It wraps wireproxy: userspace WireGuard + gvisor/netstack + SOCKS5 listener.
package mobile

import (
	"strings"
	"sync"

	"github.com/pufferffish/wireproxy"
	"github.com/pufferffish/wireproxy/config"
)

var (
	mu      sync.Mutex
	routine *wireproxy.Routine
)

func Start(confText string) string {
	mu.Lock()
	defer mu.Unlock()
	if routine != nil {
		return "already running"
	}
	devCfg, err := config.ParseConfig(strings.NewReader(confText))
	if err != nil {
		return "config: " + err.Error()
	}
	r, err := wireproxy.StartWireproxy(devCfg)
	if err != nil {
		return "start: " + err.Error()
	}
	routine = r
	return ""
}

func Stop() string {
	mu.Lock()
	defer mu.Unlock()
	if routine == nil {
		return ""
	}
	err := routine.Stop()
	routine = nil
	if err != nil {
		return err.Error()
	}
	return ""
}

func IsRunning() bool {
	mu.Lock()
	defer mu.Unlock()
	return routine != nil
}

func Version() string { return "sockwire-core-1.0.0" }
