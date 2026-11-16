// Package mobile is the gomobile entrypoint for SockWire Core.
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

// Start parses a wireproxy .conf text and starts the tunnel.
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

// Stop halts the tunnel.
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

// IsRunning reports engine state.
func IsRunning() bool {
	mu.Lock()
	defer mu.Unlock()
	return routine != nil
}

// Version is the SockWire Core version string.
func Version() string { return "sockwire-core-1.0.0" }
