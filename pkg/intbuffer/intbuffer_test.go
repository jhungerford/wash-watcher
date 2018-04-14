package intbuffer

import (
	"testing"
	"fmt"
)

func TestNew(t *testing.T) {
	capacity := 5
	expected := &Buffer{[]int {0, 0, 0, 0, 0}, 0, false}

	verifyBuffer(t, expected, New(capacity))
}

func TestAddNotFull(t *testing.T) {
	expected := New(5)
	expected.data = []int{1, 2, 3, 0, 0}
	expected.writeIndex = 3

	actual := New(5)
	for i := 1; i <= 3; i ++ {
		actual.Add(i)
	}

	verifyBuffer(t, expected, actual)
}

func TestAddFull(t *testing.T) {
	expected := New(5)
	expected.data = []int{6, 7, 8, 4, 5}
	expected.writeIndex = 3
	expected.full = true

	actual := New(5)
	for i := 1; i <= 8; i ++ {
		actual.Add(i)
	}

	verifyBuffer(t, expected, actual)
}

func TestFoldSumNotFull(t *testing.T) {
	// 1, 2, 3, 0, 0 = 6
	buffer := New(5)
	for i := 1; i <= 3; i ++ {
		buffer.Add(i)
	}

	expected := 6
	actual := buffer.Fold(sum)

	if expected != actual {
		fmt.Errorf("Incorrect sum.  Expected %d, actual %d", expected, actual)
	}
}

func TestFoldSumFull(t *testing.T) {
	// 6, 7, 8, 4, 5 = 30
	buffer := New(5)
	for i := 1; i <= 8; i ++ {
		buffer.Add(i)
	}

	expected := 30
	actual := buffer.Fold(sum)

	if expected != actual {
		fmt.Errorf("Incorrect sum.  Expected %d, actual %d", expected, actual)
	}
}

func sum(acc, value int) int {
	return acc + value
}

// Verifies the internals of the buffer
func verifyBuffer(t *testing.T, expected *Buffer, actual *Buffer) {
	if len(expected.data) != len(actual.data) {
		t.Errorf("Expected buffer capacity capacity %d, actual %d", len(expected.data), len(actual.data))
	}

	if expected.writeIndex != actual.writeIndex {
		t.Errorf("Expected buffer writeIndex %d, actual %d", expected.writeIndex, actual.writeIndex)
	}

	if expected.full != actual.full {
		t.Errorf("Expected buffer to have full %t, actual %t", expected.full, actual.full)
	}
}

// Returns true if the two arrays have the same length and values, false otherwise
func arraysEqual(expected []int, actual []int) bool {
	if len(expected) != len(actual) {
		return false
	}

	for i, val := range expected {
		if val != actual[i] {
			return false
		}
	}

	return true
}
